package com.sakcode.elearning.school.features.payment.process;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ProcessPaymentRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  void shouldCreateValidRequest() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .build();

    assertEquals(1L, request.getStudentId());
    assertEquals(100L, request.getEnrollmentId());
    assertEquals(0, new BigDecimal("49.99").compareTo(request.getAmount()));
    assertEquals("CREDIT_CARD", request.getPaymentMethod());
  }

  @Test
  void shouldFailValidationWhenEnrollmentIdIsNull() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("CREDIT_CARD")
            .build();

    Set<ConstraintViolation<ProcessPaymentRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Enrollment ID is required")));
  }

  @Test
  void shouldFailValidationWhenAmountIsNull() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .paymentMethod("CREDIT_CARD")
            .build();

    Set<ConstraintViolation<ProcessPaymentRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount is required")));
  }

  @Test
  void shouldFailValidationWhenAmountIsNegative() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("-10.00"))
            .paymentMethod("CREDIT_CARD")
            .build();

    Set<ConstraintViolation<ProcessPaymentRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Amount must be positive")));
  }

  @Test
  void shouldFailValidationWhenPaymentMethodIsBlank() {
    ProcessPaymentRequest request =
        ProcessPaymentRequest.builder()
            .studentId(1L)
            .enrollmentId(100L)
            .amount(new BigDecimal("49.99"))
            .paymentMethod("")
            .build();

    Set<ConstraintViolation<ProcessPaymentRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Payment method is required")));
  }

  @Test
  void shouldUseNoArgsConstructor() {
    ProcessPaymentRequest request = new ProcessPaymentRequest();
    assertNull(request.getStudentId());
    assertNull(request.getEnrollmentId());
    assertNull(request.getAmount());
    assertNull(request.getPaymentMethod());
  }
}
