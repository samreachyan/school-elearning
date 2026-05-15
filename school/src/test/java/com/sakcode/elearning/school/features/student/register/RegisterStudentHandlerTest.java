package com.sakcode.elearning.school.features.student.register;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RegisterStudentHandlerTest {

  @Mock private StudentRepository studentRepository;
  @Mock private PasswordEncoder passwordEncoder;

  private RegisterStudentHandler handler;

  @BeforeEach
  void setUp() {
    handler = new RegisterStudentHandler(studentRepository, passwordEncoder);
  }

  @Test
  void shouldRegisterStudentSuccessfully() {
    RegisterStudentRequest request =
        RegisterStudentRequest.builder()
            .email("john@example.com")
            .name("John Doe")
            .password("password123")
            .planType(PlanType.FREE)
            .build();

    when(studentRepository.existsByEmail("john@example.com")).thenReturn(false);
    when(passwordEncoder.encode("password123")).thenReturn("encoded-password");

    Student savedStudent = new Student("john@example.com", "John Doe", "encoded-password", PlanType.FREE);
    savedStudent.setId(1L);
    when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

    RegisterStudentResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(1L, response.getId());
    assertEquals("john@example.com", response.getEmail());
    assertEquals("John Doe", response.getName());
    assertEquals("FREE", response.getPlanType());
    assertEquals("Student registered successfully", response.getMessage());
    verify(studentRepository).existsByEmail("john@example.com");
    verify(passwordEncoder).encode("password123");
    verify(studentRepository).save(any(Student.class));
  }

  @Test
  void shouldThrowExceptionWhenEmailAlreadyExists() {
    RegisterStudentRequest request =
        RegisterStudentRequest.builder()
            .email("existing@example.com")
            .name("Existing User")
            .password("password123")
            .planType(PlanType.PREMIUM)
            .build();

    when(studentRepository.existsByEmail("existing@example.com")).thenReturn(true);

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Email already registered", exception.getMessage());
    assertEquals("EMAIL_EXISTS", exception.getErrorCode());
    verify(studentRepository, never()).save(any(Student.class));
  }
}
