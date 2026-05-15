package com.sakcode.elearning.school.features.course;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CourseTest {

  @Test
  void shouldCreateCourseUsingAllArgsConstructor() {
    Course course =
        new Course("Test Course", "Test Description", new BigDecimal("49.99"), "Dr. Instructor");

    assertEquals("Test Course", course.getTitle());
    assertEquals("Test Description", course.getDescription());
    assertEquals(0, new BigDecimal("49.99").compareTo(course.getPrice()));
    assertEquals("Dr. Instructor", course.getInstructor());
    assertNotNull(course.getCreatedAt());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    Course course = new Course();
    assertNull(course.getTitle());
    assertNull(course.getDescription());
    assertNull(course.getPrice());
    assertNull(course.getInstructor());
  }

  @Test
  void shouldSetAndGetFields() {
    Course course = new Course();
    course.setTitle("Updated Title");
    course.setDescription("Updated Description");
    course.setPrice(new BigDecimal("99.99"));
    course.setInstructor("New Instructor");

    assertEquals("Updated Title", course.getTitle());
    assertEquals("Updated Description", course.getDescription());
    assertEquals(0, new BigDecimal("99.99").compareTo(course.getPrice()));
    assertEquals("New Instructor", course.getInstructor());
  }
}
