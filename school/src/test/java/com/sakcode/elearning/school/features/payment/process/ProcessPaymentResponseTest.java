package com.sakcode.elearning.school.features.payment.process;

import static org.junit.jupiter.api.Assertions.*;

import com.sakcode.elearning.school.features.payment.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ProcessPaymentResponseTest {

  @Test
  void shouldCreateResponseWithBuilder() {
    LocalDateTime now = LocalDateTime.now();

    ProcessPaymentResponse response =
        ProcessPaymentResponse.builder()
            .paymentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .status(PaymentStatus.COMPLETED)
            .transactionId("TXN-ABC123")
            .paidAt(now)
            .message("Payment processed successfully")
            .build();

    assertEquals(1L, response.getPaymentId());
    assertEquals(100L, response.getEnrollmentId());
    assertEquals(0, new BigDecimal("49.99").compareTo(response.getAmount()));
    assertEquals("CREDIT_CARD", response.getPaymentMethod());
    assertEquals(PaymentStatus.COMPLETED, response.getStatus());
    assertEquals("TXN-ABC123", response.getTransactionId());
    assertEquals(now, response.getPaidAt());
    assertEquals("Payment processed successfully", response.getMessage());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    ProcessPaymentResponse response = new ProcessPaymentResponse();
    assertNull(response.getPaymentId());
    assertNull(response.getEnrollmentId());
    assertNull(response.getAmount());
    assertNull(response.getPaymentMethod());
    assertNull(response.getStatus());
    assertNull(response.getTransactionId());
    assertNull(response.getPaidAt());
    assertNull(response.getMessage());
  }

  @Test
  void shouldHandlePendingPaymentStatus() {
    ProcessPaymentResponse response =
        ProcessPaymentResponse.builder()
            .paymentId(2L)
            .enrollmentId(200L)
            .amount(new BigDecimal("79.99"))
            .paymentMethod("PAYPAL")
            .status(PaymentStatus.PENDING)
            .message("Payment is pending")
            .build();

    assertEquals(PaymentStatus.PENDING, response.getStatus());
    assertNull(response.getTransactionId());
    assertNull(response.getPaidAt());
  }

  @Test
  void shouldHandleFailedPaymentStatus() {
    ProcessPaymentResponse response =
        ProcessPaymentResponse.builder()
            .paymentId(3L)
            .enrollmentId(300L)
            .amount(new BigDecimal("59.99"))
            .paymentMethod("VISA")
            .status(PaymentStatus.FAILED)
            .message("Payment failed")
            .build();

    assertEquals(PaymentStatus.FAILED, response.getStatus());
    assertEquals("Payment failed", response.getMessage());
  }
}
