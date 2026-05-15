package com.sakcode.elearning.school.features.progress.marklesson;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class MarkLessonCompletedResponseTest {

  @Test
  void shouldCreateResponseUsingBuilder() {
    LocalDateTime now = LocalDateTime.now();
    MarkLessonCompletedResponse response =
        MarkLessonCompletedResponse.builder()
            .lessonCompletionId(50L)
            .enrollmentId(1L)
            .lessonId(10L)
            .completedAt(now)
            .progressPercentage(new BigDecimal("20.00"))
            .message("Lesson marked as completed")
            .build();

    assertEquals(50L, response.getLessonCompletionId());
    assertEquals(1L, response.getEnrollmentId());
    assertEquals(10L, response.getLessonId());
    assertEquals(now, response.getCompletedAt());
    assertEquals(0, new BigDecimal("20.00").compareTo(response.getProgressPercentage()));
    assertEquals("Lesson marked as completed", response.getMessage());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    MarkLessonCompletedResponse response = new MarkLessonCompletedResponse();
    assertNull(response.getLessonCompletionId());
    assertNull(response.getEnrollmentId());
    assertNull(response.getLessonId());
    assertNull(response.getCompletedAt());
    assertNull(response.getProgressPercentage());
    assertNull(response.getMessage());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    LocalDateTime now = LocalDateTime.now();
    MarkLessonCompletedResponse response =
        new MarkLessonCompletedResponse(50L, 1L, 10L, now, new BigDecimal("20.00"), "Done");

    assertEquals(50L, response.getLessonCompletionId());
    assertEquals(1L, response.getEnrollmentId());
    assertEquals(10L, response.getLessonId());
    assertEquals(now, response.getCompletedAt());
    assertEquals(0, new BigDecimal("20.00").compareTo(response.getProgressPercentage()));
    assertEquals("Done", response.getMessage());
  }

  @Test
  void shouldSetAndGetFields() {
    MarkLessonCompletedResponse response = new MarkLessonCompletedResponse();
    response.setLessonCompletionId(99L);
    response.setEnrollmentId(5L);
    response.setLessonId(15L);
    response.setCompletedAt(null);
    response.setProgressPercentage(new BigDecimal("50.00"));
    response.setMessage("Halfway");

    assertEquals(99L, response.getLessonCompletionId());
    assertEquals(5L, response.getEnrollmentId());
    assertEquals(15L, response.getLessonId());
    assertNull(response.getCompletedAt());
    assertEquals(0, new BigDecimal("50.00").compareTo(response.getProgressPercentage()));
    assertEquals("Halfway", response.getMessage());
  }
}
