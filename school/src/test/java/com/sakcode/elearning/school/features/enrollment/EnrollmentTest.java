package com.sakcode.elearning.school.features.enrollment;

import static org.junit.jupiter.api.Assertions.*;

import com.sakcode.elearning.school.features.payment.PaymentStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

  @Test
  void shouldCreateEnrollmentWithConstructor() {
    Enrollment enrollment = new Enrollment(1L, 10L);

    assertEquals(1L, enrollment.getStudentId());
    assertEquals(10L, enrollment.getCourseId());
    assertEquals(0, BigDecimal.ZERO.compareTo(enrollment.getProgressPercentage()));
    assertEquals(PaymentStatus.PENDING, enrollment.getPaymentStatus());
    assertNotNull(enrollment.getCreatedAt());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    Enrollment enrollment = new Enrollment();
    assertNull(enrollment.getStudentId());
    assertNull(enrollment.getCourseId());
    assertNull(enrollment.getProgressPercentage());
    assertNull(enrollment.getPaymentStatus());
  }

  @Test
  void shouldSetAndGetProgressPercentage() {
    Enrollment enrollment = new Enrollment(1L, 10L);
    enrollment.setProgressPercentage(new BigDecimal("50.00"));

    assertEquals(0, new BigDecimal("50.00").compareTo(enrollment.getProgressPercentage()));
  }

  @Test
  void shouldSetAndGetPaymentStatus() {
    Enrollment enrollment = new Enrollment(1L, 10L);
    enrollment.setPaymentStatus(PaymentStatus.COMPLETED);

    assertEquals(PaymentStatus.COMPLETED, enrollment.getPaymentStatus());
  }
}
