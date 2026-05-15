package com.sakcode.elearning.school.features.progress.generatecertificate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenerateCertificateHandlerTest {

  @Mock private EnrollmentRepository enrollmentRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private StudentRepository studentRepository;

  private GenerateCertificateHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GenerateCertificateHandler(enrollmentRepository, courseRepository, studentRepository);
  }

  @Test
  void shouldGenerateCertificateSuccessfully() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(1L).build();

    Enrollment enrollment = new Enrollment(1L, 100L);
    enrollment.setId(1L);
    enrollment.setProgressPercentage(new BigDecimal("100.00"));

    Course course = new Course("Java", "Java course", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(100L);

    Student student = new Student("john@example.com", "John Doe", "password", PlanType.FREE);
    student.setId(1L);

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(courseRepository.findById(100L)).thenReturn(Optional.of(course));
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    GenerateCertificateResponse response = handler.handle(request);

    assertNotNull(response);
    assertTrue(response.getCertificateId().startsWith("CERT-"));
    assertEquals(1L, response.getStudentId());
    assertEquals("John Doe", response.getStudentName());
    assertEquals(100L, response.getCourseId());
    assertEquals("Java", response.getCourseTitle());
    assertEquals("Certificate generated successfully", response.getMessage());
    assertNotNull(response.getCompletedAt());
  }

  @Test
  void shouldThrowExceptionWhenEnrollmentNotFound() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(999L).build();

    when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Enrollment not found", exception.getMessage());
    assertEquals("ENROLLMENT_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenCourseNotCompleted() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(1L).build();

    Enrollment enrollment = new Enrollment(1L, 100L);
    enrollment.setId(1L);
    enrollment.setProgressPercentage(new BigDecimal("50.00"));

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertTrue(exception.getMessage().contains("Course not yet completed"));
    assertEquals("COURSE_NOT_COMPLETED", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenCourseNotFound() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(1L).build();

    Enrollment enrollment = new Enrollment(1L, 999L);
    enrollment.setId(1L);
    enrollment.setProgressPercentage(new BigDecimal("100.00"));

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(courseRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Course not found", exception.getMessage());
    assertEquals("COURSE_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenStudentNotFound() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(1L).build();

    Enrollment enrollment = new Enrollment(999L, 100L);
    enrollment.setId(1L);
    enrollment.setProgressPercentage(new BigDecimal("100.00"));

    Course course = new Course("Java", "Desc", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(100L);

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(courseRepository.findById(100L)).thenReturn(Optional.of(course));
    when(studentRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Student not found", exception.getMessage());
    assertEquals("STUDENT_NOT_FOUND", exception.getErrorCode());
  }
}
