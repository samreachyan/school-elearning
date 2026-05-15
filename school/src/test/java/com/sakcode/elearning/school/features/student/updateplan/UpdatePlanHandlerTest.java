package com.sakcode.elearning.school.features.student.updateplan;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
class UpdatePlanHandlerTest {

  @Mock private StudentRepository studentRepository;

  private UpdatePlanHandler handler;

  @BeforeEach
  void setUp() {
    handler = new UpdatePlanHandler(studentRepository);
  }

  @Test
  void shouldUpdatePlanSuccessfully() {
    UpdatePlanRequest request =
        UpdatePlanRequest.builder().studentId(1L).planType(PlanType.PREMIUM).build();

    Student student = new Student("john@example.com", "John Doe", "password", PlanType.FREE);
    student.setId(1L);

    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    Student updatedStudent = new Student("john@example.com", "John Doe", "password", PlanType.PREMIUM);
    updatedStudent.setId(1L);
    when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

    UpdatePlanResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(1L, response.getId());
    assertEquals("john@example.com", response.getEmail());
    assertEquals("John Doe", response.getName());
    assertEquals("PREMIUM", response.getPlanType());
    assertEquals("Subscription plan updated successfully", response.getMessage());
    verify(studentRepository).findById(1L);
    verify(studentRepository).save(any(Student.class));
  }

  @Test
  void shouldThrowExceptionWhenStudentNotFound() {
    UpdatePlanRequest request =
        UpdatePlanRequest.builder().studentId(999L).planType(PlanType.PREMIUM).build();

    when(studentRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Student not found", exception.getMessage());
    assertEquals("STUDENT_NOT_FOUND", exception.getErrorCode());
    verify(studentRepository, never()).save(any(Student.class));
  }
}
