package com.sakcode.elearning.school.shared.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

  private JwtTokenProvider jwtTokenProvider;

  @BeforeEach
  void setUp() {
    jwtTokenProvider =
        new JwtTokenProvider(
            "eLearningPlatformSecretKeyForJWTTokenGeneration2026VeryLongSecureKey", 86400000);
  }

  @Test
  void shouldGenerateToken() {
    String token = jwtTokenProvider.generateToken("test@example.com", 1L);

    assertNotNull(token);
    assertFalse(token.isEmpty());
  }

  @Test
  void shouldGetEmailFromToken() {
    String token = jwtTokenProvider.generateToken("test@example.com", 1L);

    String email = jwtTokenProvider.getEmailFromToken(token);

    assertEquals("test@example.com", email);
  }

  @Test
  void shouldGetStudentIdFromToken() {
    String token = jwtTokenProvider.generateToken("test@example.com", 42L);

    Long studentId = jwtTokenProvider.getStudentIdFromToken(token);

    assertEquals(42L, studentId);
  }

  @Test
  void shouldValidateValidToken() {
    String token = jwtTokenProvider.generateToken("test@example.com", 1L);

    assertTrue(jwtTokenProvider.validateToken(token));
  }

  @Test
  void shouldInvalidateMalformedToken() {
    assertFalse(jwtTokenProvider.validateToken("invalid-token"));
  }

  @Test
  void shouldInvalidateNullToken() {
    assertFalse(jwtTokenProvider.validateToken(null));
  }

  @Test
  void shouldInvalidateEmptyToken() {
    assertFalse(jwtTokenProvider.validateToken(""));
  }
}
