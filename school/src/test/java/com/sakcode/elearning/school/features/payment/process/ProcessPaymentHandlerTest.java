package com.sakcode.elearning.school.features.payment.process;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.payment.Payment;
import com.sakcode.elearning.school.features.payment.PaymentRepository;
import com.sakcode.elearning.school.features.payment.PaymentStatus;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentHandlerTest {

  @Mock private PaymentRepository paymentRepository;
  @Mock private EnrollmentRepository enrollmentRepository;

  @Captor private ArgumentCaptor<Payment> paymentCaptor;
  @Captor private ArgumentCaptor<Enrollment> enrollmentCaptor;

  private ProcessPaymentHandler handler;

  @BeforeEach
  void setUp() {
    handler = new ProcessPaymentHandler(paymentRepository, enrollmentRepository);
  }

  @Test
  void shouldProcessPaymentSuccessfully() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .build();

    Enrollment enrollment = new Enrollment(1L, 10L);
    enrollment.setId(100L);
    enrollment.setPaymentStatus(PaymentStatus.PENDING);

    when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));

    Payment savedPayment = new Payment(100L, 1L, new BigDecimal("49.99"), "CREDIT_CARD");
    savedPayment.setId(999L);
    savedPayment.setStatus(PaymentStatus.COMPLETED);
    savedPayment.setTransactionId("TXN-ABC123");
    savedPayment.setPaidAt(java.time.LocalDateTime.now());
    when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

    ProcessPaymentResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(999L, response.getPaymentId());
    assertEquals(100L, response.getEnrollmentId());
    assertEquals(0, new BigDecimal("49.99").compareTo(response.getAmount()));
    assertEquals("CREDIT_CARD", response.getPaymentMethod());
    assertEquals(PaymentStatus.COMPLETED, response.getStatus());
    assertNotNull(response.getTransactionId());
    assertNotNull(response.getPaidAt());
    assertEquals("Payment processed successfully", response.getMessage());

    verify(paymentRepository).save(any(Payment.class));
    verify(enrollmentRepository).save(enrollmentCaptor.capture());
    assertEquals(PaymentStatus.COMPLETED, enrollmentCaptor.getValue().getPaymentStatus());
  }

  @Test
  void shouldThrowExceptionWhenEnrollmentNotFound() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(999L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .build();

    when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Enrollment not found", exception.getMessage());
    assertEquals("ENROLLMENT_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenEnrollmentDoesNotBelongToStudent() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(2L)
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .build();

    Enrollment enrollment = new Enrollment(1L, 10L); // studentId = 1, not 2
    enrollment.setId(100L);

    when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Enrollment does not belong to this student", exception.getMessage());
    assertEquals("INVALID_ENROLLMENT", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenPaymentAlreadyCompleted() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .build();

    Enrollment enrollment = new Enrollment(1L, 10L);
    enrollment.setId(100L);
    enrollment.setPaymentStatus(PaymentStatus.COMPLETED); // Already paid

    when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Payment already completed for this enrollment", exception.getMessage());
    assertEquals("PAYMENT_ALREADY_COMPLETED", exception.getErrorCode());
  }

  @Test
  void shouldGenerateTransactionId() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("29.99"))
            .paymentMethod("PAYPAL")
            .build();

    Enrollment enrollment = new Enrollment(1L, 10L);
    enrollment.setId(100L);
    enrollment.setPaymentStatus(PaymentStatus.PENDING);

    when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));
    when(paymentRepository.save(any(Payment.class)))
        .thenAnswer(
            invocation -> {
              Payment p = invocation.getArgument(0);
              p.setId(888L);
              return p;
            });
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

    ProcessPaymentResponse response = handler.handle(request);

    assertNotNull(response.getTransactionId());
    assertTrue(response.getTransactionId().startsWith("TXN-"));
    assertEquals(12, response.getTransactionId().length()); // "TXN-" + 8 chars
  }

  @Test
  void shouldHandleDifferentPaymentMethods() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("79.99"))
            .paymentMethod("VISA")
            .build();

    Enrollment enrollment = new Enrollment(1L, 10L);
    enrollment.setId(100L);
    enrollment.setPaymentStatus(PaymentStatus.PENDING);

    when(enrollmentRepository.findById(100L)).thenReturn(Optional.of(enrollment));
    when(paymentRepository.save(any(Payment.class)))
        .thenAnswer(
            invocation -> {
              Payment p = invocation.getArgument(0);
              p.setId(777L);
              p.setStatus(PaymentStatus.COMPLETED);
              p.setTransactionId("TXN-TEST123");
              p.setPaidAt(java.time.LocalDateTime.now());
              return p;
            });
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

    ProcessPaymentResponse response = handler.handle(request);

    assertEquals("VISA", response.getPaymentMethod());
    assertEquals(PaymentStatus.COMPLETED, response.getStatus());
  }
}
