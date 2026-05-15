package com.sakcode.elearning.school.features.progress.marklesson;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MarkLessonCompletedRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldPassValidationWhenAllFieldsAreValid() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(1L).lessonId(10L).build();

    var violations = validator.validate(request);
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldFailValidationWhenEnrollmentIdIsNull() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(null).lessonId(10L).build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Enrollment ID is required")));
  }

  @Test
  void shouldFailValidationWhenLessonIdIsNull() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(1L).lessonId(null).build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Lesson ID is required")));
  }

  @Test
  void shouldUseBuilderAndSetters() {
    MarkLessonCompletedRequest request = new MarkLessonCompletedRequest();
    request.setEnrollmentId(5L);
    request.setLessonId(20L);

    assertEquals(5L, request.getEnrollmentId());
    assertEquals(20L, request.getLessonId());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    MarkLessonCompletedRequest request = new MarkLessonCompletedRequest();
    assertNull(request.getEnrollmentId());
    assertNull(request.getLessonId());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    MarkLessonCompletedRequest request = new MarkLessonCompletedRequest(1L, 10L);
    assertEquals(1L, request.getEnrollmentId());
    assertEquals(10L, request.getLessonId());
  }
}
