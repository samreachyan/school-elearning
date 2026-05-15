package com.sakcode.elearning.school.shared.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

  @Test
  void shouldCreateErrorResponseUsingBuilder() {
    LocalDateTime now = LocalDateTime.now();
    ErrorResponse response =
        ErrorResponse.builder()
            .status(400)
            .error("Bad Request")
            .message("Validation failed")
            .errorCode("VALIDATION_ERROR")
            .timestamp(now)
            .path("/api/v1/test")
            .build();

    assertEquals(400, response.getStatus());
    assertEquals("Bad Request", response.getError());
    assertEquals("Validation failed", response.getMessage());
    assertEquals("VALIDATION_ERROR", response.getErrorCode());
    assertEquals(now, response.getTimestamp());
    assertEquals("/api/v1/test", response.getPath());
  }

  @Test
  void shouldCreateErrorResponseWithValidationErrors() {
    ErrorResponse.ValidationError error =
        ErrorResponse.ValidationError.builder().field("email").message("Email is required").build();

    assertEquals("email", error.getField());
    assertEquals("Email is required", error.getMessage());
  }

  @Test
  void shouldCreateErrorResponseWithMultipleValidationErrors() {
    ErrorResponse.ValidationError error1 =
        ErrorResponse.ValidationError.builder().field("email").message("Email is required").build();
    ErrorResponse.ValidationError error2 =
        ErrorResponse.ValidationError.builder()
            .field("password")
            .message("Password is required")
            .build();

    ErrorResponse response =
        ErrorResponse.builder()
            .status(400)
            .error("Bad Request")
            .message("Validation failed")
            .errorCode("VALIDATION_ERROR")
            .timestamp(LocalDateTime.now())
            .path("/api/v1/test")
            .validationErrors(List.of(error1, error2))
            .build();

    assertEquals(2, response.getValidationErrors().size());
    assertEquals("email", response.getValidationErrors().get(0).getField());
    assertEquals("password", response.getValidationErrors().get(1).getField());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    ErrorResponse response = new ErrorResponse();
    assertEquals(0, response.getStatus());
    assertNull(response.getError());
    assertNull(response.getMessage());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    LocalDateTime now = LocalDateTime.now();
    ErrorResponse response = new ErrorResponse(500, "Error", "msg", "CODE", now, "/path", null);

    assertEquals(500, response.getStatus());
    assertEquals("Error", response.getError());
    assertEquals("msg", response.getMessage());
    assertEquals("CODE", response.getErrorCode());
    assertEquals(now, response.getTimestamp());
    assertEquals("/path", response.getPath());
  }
}
