package com.sakcode.elearning.school.features.progress.getprogress;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GetCourseProgressRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldPassValidationWhenEnrollmentIdIsValid() {
    GetCourseProgressRequest request =
        GetCourseProgressRequest.builder().enrollmentId(1L).build();

    var violations = validator.validate(request);
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldFailValidationWhenEnrollmentIdIsNull() {
    GetCourseProgressRequest request =
        GetCourseProgressRequest.builder().enrollmentId(null).build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Enrollment ID is required")));
  }

  @Test
  void shouldUseBuilderAndSetters() {
    GetCourseProgressRequest request = new GetCourseProgressRequest();
    request.setEnrollmentId(5L);

    assertEquals(5L, request.getEnrollmentId());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    GetCourseProgressRequest request = new GetCourseProgressRequest();
    assertNull(request.getEnrollmentId());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    GetCourseProgressRequest request = new GetCourseProgressRequest(1L);
    assertEquals(1L, request.getEnrollmentId());
  }
}
