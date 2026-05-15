package com.sakcode.elearning.school.features.payment;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PaymentTest {

  @Test
  void shouldCreatePaymentWithConstructor() {
    Payment payment = new Payment(1L, 10L, new BigDecimal("49.99"), "CREDIT_CARD");

    assertEquals(1L, payment.getEnrollmentId());
    assertEquals(10L, payment.getStudentId());
    assertEquals(0, new BigDecimal("49.99").compareTo(payment.getAmount()));
    assertEquals("CREDIT_CARD", payment.getPaymentMethod());
    assertEquals(PaymentStatus.PENDING, payment.getStatus());
    assertNull(payment.getTransactionId());
    assertNull(payment.getPaidAt());
    assertNotNull(payment.getCreatedAt());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    Payment payment = new Payment();
    assertNull(payment.getEnrollmentId());
    assertNull(payment.getStudentId());
    assertNull(payment.getAmount());
    assertNull(payment.getPaymentMethod());
    assertNull(payment.getStatus());
  }

  @Test
  void shouldSetAndGetPaymentStatus() {
    Payment payment = new Payment(1L, 10L, new BigDecimal("29.99"), "PAYPAL");
    payment.setStatus(PaymentStatus.COMPLETED);

    assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
  }

  @Test
  void shouldSetAndGetTransactionId() {
    Payment payment = new Payment(1L, 10L, new BigDecimal("79.99"), "VISA");
    payment.setTransactionId("TXN-ABC12345");

    assertEquals("TXN-ABC12345", payment.getTransactionId());
  }

  @Test
  void shouldSetAndGetPaidAt() {
    Payment payment = new Payment(1L, 10L, new BigDecimal("59.99"), "MASTERCARD");
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    payment.setPaidAt(now);

    assertEquals(now, payment.getPaidAt());
  }

  @Test
  void shouldSupportAllPaymentStatuses() {
    Payment payment = new Payment(1L, 10L, BigDecimal.TEN, "CARD");

    payment.setStatus(PaymentStatus.PENDING);
    assertEquals(PaymentStatus.PENDING, payment.getStatus());

    payment.setStatus(PaymentStatus.COMPLETED);
    assertEquals(PaymentStatus.COMPLETED, payment.getStatus());

    payment.setStatus(PaymentStatus.FAILED);
    assertEquals(PaymentStatus.FAILED, payment.getStatus());

    payment.setStatus(PaymentStatus.REFUNDED);
    assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
  }
}
