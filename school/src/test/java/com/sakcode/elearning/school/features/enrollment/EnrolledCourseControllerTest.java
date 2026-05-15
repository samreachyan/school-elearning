package com.sakcode.elearning.school.features.enrollment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.payment.PaymentStatus;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EnrolledCourseControllerTest {

  @Mock private EnrollmentRepository enrollmentRepository;
  @Mock private CourseRepository courseRepository;

  private EnrolledCourseController controller;

  @BeforeEach
  void setUp() {
    controller = new EnrolledCourseController(enrollmentRepository, courseRepository);
  }

  @Test
  void shouldGetEnrolledCourses() {
    StudentPrincipal principal = new StudentPrincipal("test@example.com", 1L);

    Enrollment enrollment = new Enrollment(1L, 10L);
    enrollment.setId(100L);
    enrollment.setProgressPercentage(new BigDecimal("50.00"));

    Course course = new Course("Java", "Java desc", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(10L);

    when(enrollmentRepository.findByStudentId(1L)).thenReturn(List.of(enrollment));
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

    ResponseEntity<List<EnrolledCourseDto>> response = controller.getEnrolledCourses(principal);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    List<EnrolledCourseDto> body = response.getBody();
    assertNotNull(body);
    assertEquals(1, body.size());
    assertEquals(100L, body.get(0).getEnrollmentId());
    assertEquals(10L, body.get(0).getCourseId());
    assertEquals("Java", body.get(0).getCourseTitle());
    assertEquals("Java desc", body.get(0).getCourseDescription());
    assertEquals("Dr. Smith", body.get(0).getInstructor());
    assertEquals(0, new BigDecimal("49.99").compareTo(body.get(0).getPrice()));
    assertEquals(0, new BigDecimal("50.00").compareTo(body.get(0).getProgressPercentage()));
    assertEquals(PaymentStatus.PENDING, body.get(0).getPaymentStatus());
    assertNotNull(body.get(0).getEnrollmentDate());
  }

  @Test
  void shouldReturnEmptyListWhenNoEnrollments() {
    StudentPrincipal principal = new StudentPrincipal("test@example.com", 1L);

    when(enrollmentRepository.findByStudentId(1L)).thenReturn(List.of());

    ResponseEntity<List<EnrolledCourseDto>> response = controller.getEnrolledCourses(principal);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    List<EnrolledCourseDto> body = response.getBody();
    assertNotNull(body);
    assertTrue(body.isEmpty());
  }

  @Test
  void shouldHandleMissingCourse() {
    StudentPrincipal principal = new StudentPrincipal("test@example.com", 1L);

    Enrollment enrollment = new Enrollment(1L, 999L);
    enrollment.setId(100L);

    when(enrollmentRepository.findByStudentId(1L)).thenReturn(List.of(enrollment));
    when(courseRepository.findById(999L)).thenReturn(Optional.empty());

    ResponseEntity<List<EnrolledCourseDto>> response = controller.getEnrolledCourses(principal);

    assertNotNull(response);
    List<EnrolledCourseDto> body = response.getBody();
    assertNotNull(body);
    assertEquals(1, body.size());
    assertEquals("Unknown", body.get(0).getCourseTitle());
    assertEquals("", body.get(0).getCourseDescription());
    assertEquals("", body.get(0).getInstructor());
    assertNull(body.get(0).getPrice());
  }
}
