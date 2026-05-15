package com.sakcode.elearning.school.features.student;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StudentTest {

  @Test
  void shouldCreateStudentWithConstructor() {
    Student student = new Student("test@example.com", "John Doe", "password123", PlanType.FREE);

    assertEquals("test@example.com", student.getEmail());
    assertEquals("John Doe", student.getName());
    assertEquals("password123", student.getPassword());
    assertEquals(PlanType.FREE, student.getPlanType());
    assertNotNull(student.getCreatedAt());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    Student student = new Student();
    assertNull(student.getEmail());
    assertNull(student.getName());
    assertNull(student.getPassword());
    assertNull(student.getPlanType());
  }

  @Test
  void shouldSetAndGetFields() {
    Student student = new Student();
    student.setEmail("updated@example.com");
    student.setName("Jane Doe");
    student.setPassword("newpassword");
    student.setPlanType(PlanType.PREMIUM);

    assertEquals("updated@example.com", student.getEmail());
    assertEquals("Jane Doe", student.getName());
    assertEquals("newpassword", student.getPassword());
    assertEquals(PlanType.PREMIUM, student.getPlanType());
  }
}
