package com.sakcode.elearning.school.features.course.enroll;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.payment.PaymentStatus;
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
class EnrollCourseHandlerTest {

  @Mock private EnrollmentRepository enrollmentRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private StudentRepository studentRepository;

  private EnrollCourseHandler handler;

  @BeforeEach
  void setUp() {
    handler = new EnrollCourseHandler(enrollmentRepository, courseRepository, studentRepository);
  }

  @Test
  void shouldEnrollFreePlanStudentInPaidCourseWithPendingPayment() {
    EnrollCourseRequest request = EnrollCourseRequest.builder().studentId(1L).courseId(10L).build();

    Course course = new Course("Java", "Desc", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(10L);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

    Student student = new Student("john@test.com", "John", "pass", PlanType.FREE);
    student.setId(1L);
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L)).thenReturn(false);

    Enrollment savedEnrollment = new Enrollment(1L, 10L);
    savedEnrollment.setId(100L);
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

    EnrollCourseResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(100L, response.getEnrollmentId());
    assertEquals(1L, response.getStudentId());
    assertEquals(10L, response.getCourseId());
    assertEquals(PaymentStatus.PENDING, response.getPaymentStatus());
    assertEquals(new BigDecimal("49.99"), response.getAmount());
    assertTrue(response.getMessage().contains("Payment"));
    assertNotNull(response.getEnrollmentDate());
    verify(courseRepository).findById(10L);
    verify(studentRepository).findById(1L);
    verify(enrollmentRepository).existsByStudentIdAndCourseId(1L, 10L);
    verify(enrollmentRepository).save(any(Enrollment.class));
  }

  @Test
  void shouldEnrollPremiumPlanStudentInPaidCourseWithCompletedPayment() {
    EnrollCourseRequest request = EnrollCourseRequest.builder().studentId(1L).courseId(10L).build();

    Course course = new Course("Java", "Desc", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(10L);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

    Student student = new Student("jane@test.com", "Jane", "pass", PlanType.PREMIUM);
    student.setId(1L);
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L)).thenReturn(false);

    Enrollment savedEnrollment = new Enrollment(1L, 10L);
    savedEnrollment.setId(100L);
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

    EnrollCourseResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(PaymentStatus.COMPLETED, response.getPaymentStatus());
    assertTrue(response.getMessage().contains("PREMIUM plan benefit"));
  }

  @Test
  void shouldEnrollStudentInFreeCourseWithCompletedPayment() {
    EnrollCourseRequest request = EnrollCourseRequest.builder().studentId(1L).courseId(10L).build();

    Course course = new Course("Python", "Desc", BigDecimal.ZERO, "Guido");
    course.setId(10L);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

    Student student = new Student("john@test.com", "John", "pass", PlanType.FREE);
    student.setId(1L);
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L)).thenReturn(false);

    Enrollment savedEnrollment = new Enrollment(1L, 10L);
    savedEnrollment.setId(100L);
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

    EnrollCourseResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(PaymentStatus.COMPLETED, response.getPaymentStatus());
    assertTrue(response.getMessage().contains("free course"));
  }

  @Test
  void shouldThrowExceptionWhenCourseNotFound() {
    EnrollCourseRequest request =
        EnrollCourseRequest.builder().studentId(1L).courseId(999L).build();

    when(courseRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Course not found", exception.getMessage());
    assertEquals("COURSE_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenStudentNotFound() {
    EnrollCourseRequest request = EnrollCourseRequest.builder().studentId(1L).courseId(10L).build();

    Course course = new Course("Java", "Desc", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(10L);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
    when(studentRepository.findById(1L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Student not found", exception.getMessage());
    assertEquals("STUDENT_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenAlreadyEnrolled() {
    EnrollCourseRequest request = EnrollCourseRequest.builder().studentId(1L).courseId(10L).build();

    Course course = new Course("Java", "Desc", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(10L);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

    Student student = new Student("john@test.com", "John", "pass", PlanType.FREE);
    student.setId(1L);
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L)).thenReturn(true);

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Student is already enrolled in this course", exception.getMessage());
    assertEquals("ALREADY_ENROLLED", exception.getErrorCode());
  }
}
