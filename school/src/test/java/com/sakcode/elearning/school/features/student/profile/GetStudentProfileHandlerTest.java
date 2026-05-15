package com.sakcode.elearning.school.features.student.profile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetStudentProfileHandlerTest {

  @Mock private StudentRepository studentRepository;

  private GetStudentProfileHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GetStudentProfileHandler(studentRepository);
  }

  @Test
  void shouldGetStudentProfileSuccessfully() {
    GetStudentProfileRequest request =
        GetStudentProfileRequest.builder().studentId(1L).build();

    Student student = new Student("john@example.com", "John Doe", "password", PlanType.PREMIUM);
    student.setId(1L);

    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    GetStudentProfileResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(1L, response.getId());
    assertEquals("john@example.com", response.getEmail());
    assertEquals("John Doe", response.getName());
    assertEquals("PREMIUM", response.getPlanType());
    assertNotNull(response.getCreatedAt());
  }

  @Test
  void shouldThrowExceptionWhenStudentNotFound() {
    GetStudentProfileRequest request =
        GetStudentProfileRequest.builder().studentId(999L).build();

    when(studentRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Student not found", exception.getMessage());
    assertEquals("STUDENT_NOT_FOUND", exception.getErrorCode());
  }
}
