package com.sakcode.elearning.school.shared.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @Mock private HttpServletRequest request;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
    when(request.getRequestURI()).thenReturn("/api/v1/test");
  }

  @Test
  void shouldHandleBusinessException() {
    BusinessException ex = new BusinessException("Test error", "TEST_ERROR");

    ResponseEntity<ErrorResponse> response = handler.handleBusinessException(ex, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(400, body.getStatus());
    assertEquals("Bad Request", body.getError());
    assertEquals("Test error", body.getMessage());
    assertEquals("TEST_ERROR", body.getErrorCode());
    assertEquals("/api/v1/test", body.getPath());
    assertNotNull(body.getTimestamp());
  }

  @Test
  void shouldHandleUsernameNotFoundException() {
    UsernameNotFoundException ex = new UsernameNotFoundException("User not found");

    ResponseEntity<ErrorResponse> response = handler.handleUsernameNotFoundException(ex, request);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    ErrorResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(404, body.getStatus());
    assertEquals("Not Found", body.getError());
    assertEquals("User not found", body.getMessage());
    assertEquals("USER_NOT_FOUND", body.getErrorCode());
  }

  @Test
  void shouldHandleBadCredentialsException() {
    BadCredentialsException ex = new BadCredentialsException("Bad credentials");

    ResponseEntity<ErrorResponse> response = handler.handleBadCredentialsException(ex, request);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    ErrorResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(401, body.getStatus());
    assertEquals("Unauthorized", body.getError());
    assertEquals("Invalid email or password", body.getMessage());
    assertEquals("INVALID_CREDENTIALS", body.getErrorCode());
  }

  @Test
  void shouldHandleValidationException() {
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError1 = new FieldError("object", "email", "Email is required");
    FieldError fieldError2 = new FieldError("object", "password", "Password is required");
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

    MethodArgumentNotValidException ex =
        new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponse body = response.getBody();
    assertNotNull(body);
    assertEquals("Validation failed", body.getMessage());
    assertEquals("VALIDATION_ERROR", body.getErrorCode());
    assertNotNull(body.getValidationErrors());
    assertEquals(2, body.getValidationErrors().size());
    assertEquals("email", body.getValidationErrors().get(0).getField());
    assertEquals("Email is required", body.getValidationErrors().get(0).getMessage());
    assertEquals("password", body.getValidationErrors().get(1).getField());
  }

  @Test
  void shouldHandleIllegalArgumentException() {
    IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

    ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(ex, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponse body = response.getBody();
    assertNotNull(body);
    assertEquals("Invalid argument", body.getMessage());
    assertEquals("ILLEGAL_ARGUMENT", body.getErrorCode());
  }

  @Test
  void shouldHandleGenericException() {
    Exception ex = new Exception("Unexpected error");

    ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, request);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    ErrorResponse body = response.getBody();
    assertNotNull(body);
    assertEquals("An unexpected error occurred", body.getMessage());
    assertEquals("INTERNAL_ERROR", body.getErrorCode());
  }
}
