package com.sakcode.elearning.school.features.course.create;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CreateCourseRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldPassValidationWhenAllFieldsAreValid() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Java Course")
            .description("Learn Java")
            .price(new BigDecimal("49.99"))
            .instructor("Dr. Smith")
            .build();

    var violations = validator.validate(request);
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldFailValidationWhenTitleIsBlank() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("")
            .description("Learn Java")
            .price(new BigDecimal("49.99"))
            .instructor("Dr. Smith")
            .build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Title is required")));
  }

  @Test
  void shouldFailValidationWhenDescriptionIsBlank() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Java Course")
            .description("")
            .price(new BigDecimal("49.99"))
            .instructor("Dr. Smith")
            .build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Description is required")));
  }

  @Test
  void shouldFailValidationWhenPriceIsNull() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Java Course")
            .description("Learn Java")
            .price(null)
            .instructor("Dr. Smith")
            .build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price is required")));
  }

  @Test
  void shouldFailValidationWhenPriceIsZero() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Java Course")
            .description("Learn Java")
            .price(BigDecimal.ZERO)
            .instructor("Dr. Smith")
            .build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Price must be greater than zero")));
  }

  @Test
  void shouldFailValidationWhenInstructorIsBlank() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Java Course")
            .description("Learn Java")
            .price(new BigDecimal("49.99"))
            .instructor("")
            .build();

    var violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().contains("Instructor is required")));
  }

  @Test
  void shouldUseBuilderAndSetters() {
    CreateCourseRequest request = new CreateCourseRequest();
    request.setTitle("Title");
    request.setDescription("Desc");
    request.setPrice(new BigDecimal("10"));
    request.setInstructor("Inst");

    assertEquals("Title", request.getTitle());
    assertEquals("Desc", request.getDescription());
    assertEquals(0, new BigDecimal("10").compareTo(request.getPrice()));
    assertEquals("Inst", request.getInstructor());
  }
}
