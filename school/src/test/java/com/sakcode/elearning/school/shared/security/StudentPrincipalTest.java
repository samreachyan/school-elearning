package com.sakcode.elearning.school.shared.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StudentPrincipalTest {

  @Test
  void shouldCreateStudentPrincipal() {
    StudentPrincipal principal = new StudentPrincipal("test@example.com", 1L);

    assertEquals("test@example.com", principal.getEmail());
    assertEquals(1L, principal.getStudentId());
    assertEquals("test@example.com", principal.getName());
  }

  @Test
  void shouldReturnEmailAsName() {
    StudentPrincipal principal = new StudentPrincipal("user@example.com", 42L);

    assertEquals("user@example.com", principal.getName());
  }
}
