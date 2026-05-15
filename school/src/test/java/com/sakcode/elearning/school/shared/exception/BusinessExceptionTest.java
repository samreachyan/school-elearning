package com.sakcode.elearning.school.shared.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

  @Test
  void shouldCreateBusinessExceptionWithMessageOnly() {
    BusinessException exception = new BusinessException("Test error message");

    assertEquals("Test error message", exception.getMessage());
    assertEquals("BUSINESS_ERROR", exception.getErrorCode());
  }

  @Test
  void shouldCreateBusinessExceptionWithMessageAndErrorCode() {
    BusinessException exception = new BusinessException("Test error message", "CUSTOM_CODE");

    assertEquals("Test error message", exception.getMessage());
    assertEquals("CUSTOM_CODE", exception.getErrorCode());
  }

  @Test
  void shouldCreateBusinessExceptionWithMessageAndCause() {
    Throwable cause = new RuntimeException("Root cause");
    BusinessException exception = new BusinessException("Test error message", cause);

    assertEquals("Test error message", exception.getMessage());
    assertEquals("BUSINESS_ERROR", exception.getErrorCode());
    assertEquals(cause, exception.getCause());
  }
}
