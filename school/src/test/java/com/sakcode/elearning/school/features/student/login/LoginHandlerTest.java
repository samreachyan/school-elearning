package com.sakcode.elearning.school.features.student.login;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.security.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class LoginHandlerTest {

  @Mock private StudentRepository studentRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtTokenProvider jwtTokenProvider;

  private LoginHandler handler;

  @BeforeEach
  void setUp() {
    handler = new LoginHandler(studentRepository, passwordEncoder, jwtTokenProvider);
  }

  @Test
  void shouldLoginSuccessfully() {
    LoginRequest request =
        LoginRequest.builder().email("john@example.com").password("password123").build();

    Student student =
        new Student("john@example.com", "John Doe", "encoded-password", PlanType.FREE);
    student.setId(1L);

    when(studentRepository.findByEmail("john@example.com")).thenReturn(Optional.of(student));
    when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);
    when(jwtTokenProvider.generateToken("john@example.com", 1L)).thenReturn("jwt-token");

    LoginResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals("jwt-token", response.getToken());
    assertEquals("Bearer", response.getTokenType());
    assertEquals(1L, response.getStudentId());
    assertEquals("john@example.com", response.getEmail());
    assertEquals("John Doe", response.getName());
    assertEquals("FREE", response.getPlanType());
  }

  @Test
  void shouldThrowExceptionWhenEmailNotFound() {
    LoginRequest request =
        LoginRequest.builder().email("unknown@example.com").password("password123").build();

    when(studentRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Invalid email or password", exception.getMessage());
    assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenPasswordDoesNotMatch() {
    LoginRequest request =
        LoginRequest.builder().email("john@example.com").password("wrong-password").build();

    Student student =
        new Student("john@example.com", "John Doe", "encoded-password", PlanType.FREE);
    student.setId(1L);

    when(studentRepository.findByEmail("john@example.com")).thenReturn(Optional.of(student));
    when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Invalid email or password", exception.getMessage());
    assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
  }
}
