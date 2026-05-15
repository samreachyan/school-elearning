package com.sakcode.elearning.school.features.lesson;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LessonTest {

  @Test
  void shouldCreateLessonWithConstructor() {
    Lesson lesson = new Lesson(1L, "Introduction", "Content here", 1);

    assertEquals(1L, lesson.getCourseId());
    assertEquals("Introduction", lesson.getTitle());
    assertEquals("Content here", lesson.getContent());
    assertEquals(1, lesson.getOrderNumber());
    assertNotNull(lesson.getCreatedAt());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    Lesson lesson = new Lesson();
    assertNull(lesson.getCourseId());
    assertNull(lesson.getTitle());
    assertNull(lesson.getContent());
    assertNull(lesson.getOrderNumber());
  }

  @Test
  void shouldSetAndGetFields() {
    Lesson lesson = new Lesson();
    lesson.setCourseId(2L);
    lesson.setTitle("Advanced");
    lesson.setContent("Advanced content");
    lesson.setOrderNumber(3);

    assertEquals(2L, lesson.getCourseId());
    assertEquals("Advanced", lesson.getTitle());
    assertEquals("Advanced content", lesson.getContent());
    assertEquals(3, lesson.getOrderNumber());
  }
}
