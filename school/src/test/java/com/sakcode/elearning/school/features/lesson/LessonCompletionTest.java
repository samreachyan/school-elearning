package com.sakcode.elearning.school.features.lesson;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LessonCompletionTest {

  @Test
  void shouldCreateLessonCompletionWithConstructor() {
    LessonCompletion completion = new LessonCompletion(1L, 10L);

    assertEquals(1L, completion.getEnrollmentId());
    assertEquals(10L, completion.getLessonId());
    assertNotNull(completion.getCompletedAt());
    assertNotNull(completion.getCreatedAt());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    LessonCompletion completion = new LessonCompletion();
    assertNull(completion.getEnrollmentId());
    assertNull(completion.getLessonId());
    assertNull(completion.getCompletedAt());
  }

  @Test
  void shouldSetAndGetFields() {
    LessonCompletion completion = new LessonCompletion();
    completion.setEnrollmentId(5L);
    completion.setLessonId(20L);

    assertEquals(5L, completion.getEnrollmentId());
    assertEquals(20L, completion.getLessonId());
  }
}
