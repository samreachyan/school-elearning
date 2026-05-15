package com.sakcode.elearning.school.features.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.payment.process.ProcessPaymentRequest;
import com.sakcode.elearning.school.features.payment.process.ProcessPaymentResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

  @Mock private Mediator mediator;

  private PaymentController controller;

  @BeforeEach
  void setUp() {
    controller = new PaymentController(mediator);
  }

  @Test
  void shouldProcessPaymentAndReturnCreated() {
    StudentPrincipal principal = new StudentPrincipal("john@test.com", 1L);

    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .build();

    ProcessPaymentResponse expectedResponse =
        ProcessPaymentResponse.builder()
            .paymentId(999L)
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .status(PaymentStatus.COMPLETED)
            .transactionId("TXN-ABC123")
            .message("Payment processed successfully")
            .build();

    when(mediator.send(any(ProcessPaymentRequest.class))).thenReturn(expectedResponse);

    ResponseEntity<ProcessPaymentResponse> response = controller.processPayment(principal, request);

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    ProcessPaymentResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(999L, body.getPaymentId());
    assertEquals(100L, body.getEnrollmentId());
    assertEquals(PaymentStatus.COMPLETED, body.getStatus());
    assertEquals("TXN-ABC123", body.getTransactionId());

    // Verify studentId was set from principal
    verify(mediator).send(request);
    assertEquals(1L, request.getStudentId());
  }

  @Test
  void shouldHandlePaymentWithDifferentAmounts() {
    StudentPrincipal principal = new StudentPrincipal("jane@test.com", 2L);

    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .enrollmentId(200L)
            .amount(new BigDecimal("79.99"))
            .paymentMethod("PAYPAL")
            .build();

    ProcessPaymentResponse expectedResponse =
        ProcessPaymentResponse.builder()
            .paymentId(888L)
            .enrollmentId(200L)
            .amount(new BigDecimal("79.99"))
            .paymentMethod("PAYPAL")
            .status(PaymentStatus.COMPLETED)
            .transactionId("TXN-DEF456")
            .message("Payment processed successfully")
            .build();

    when(mediator.send(any(ProcessPaymentRequest.class))).thenReturn(expectedResponse);

    ResponseEntity<ProcessPaymentResponse> response = controller.processPayment(principal, request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    ProcessPaymentResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(0, new BigDecimal("79.99").compareTo(body.getAmount()));
    assertEquals("PAYPAL", body.getPaymentMethod());
    assertEquals(2L, request.getStudentId());
  }
}
