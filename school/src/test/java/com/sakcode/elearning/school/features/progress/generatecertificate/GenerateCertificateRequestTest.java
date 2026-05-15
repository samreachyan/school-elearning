package com.sakcode.elearning.school.features.progress.generatecertificate;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GenerateCertificateRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldPassValidationWhenEnrollmentIdIsValid() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(1L).build();

    var violations = validator.validate(request);
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldFailValidationWhenEnrollmentIdIsNull() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(null).build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Enrollment ID is required")));
  }

  @Test
  void shouldUseBuilderAndSetters() {
    GenerateCertificateRequest request = new GenerateCertificateRequest();
    request.setEnrollmentId(5L);

    assertEquals(5L, request.getEnrollmentId());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    GenerateCertificateRequest request = new GenerateCertificateRequest();
    assertNull(request.getEnrollmentId());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    GenerateCertificateRequest request = new GenerateCertificateRequest(1L);
    assertEquals(1L, request.getEnrollmentId());
  }
}
