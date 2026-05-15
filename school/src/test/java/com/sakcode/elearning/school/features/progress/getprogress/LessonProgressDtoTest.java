package com.sakcode.elearning.school.features.progress.getprogress;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class LessonProgressDtoTest {

  @Test
  void shouldCreateDtoUsingBuilder() {
    LocalDateTime now = LocalDateTime.now();
    LessonProgressDto dto =
        LessonProgressDto.builder()
            .lessonId(1L)
            .title("Lesson 1")
            .orderNumber(1)
            .completed(true)
            .completedAt(now)
            .build();

    assertEquals(1L, dto.getLessonId());
    assertEquals("Lesson 1", dto.getTitle());
    assertEquals(1, dto.getOrderNumber());
    assertTrue(dto.isCompleted());
    assertEquals(now, dto.getCompletedAt());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    LessonProgressDto dto = new LessonProgressDto();
    assertNull(dto.getLessonId());
    assertNull(dto.getTitle());
    assertEquals(0, dto.getOrderNumber());
    assertFalse(dto.isCompleted());
    assertNull(dto.getCompletedAt());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    LocalDateTime now = LocalDateTime.now();
    LessonProgressDto dto = new LessonProgressDto(1L, "Lesson 1", 1, true, now);

    assertEquals(1L, dto.getLessonId());
    assertEquals("Lesson 1", dto.getTitle());
    assertEquals(1, dto.getOrderNumber());
    assertTrue(dto.isCompleted());
    assertEquals(now, dto.getCompletedAt());
  }

  @Test
  void shouldSetAndGetFields() {
    LessonProgressDto dto = new LessonProgressDto();
    dto.setLessonId(2L);
    dto.setTitle("Lesson 2");
    dto.setOrderNumber(2);
    dto.setCompleted(false);
    dto.setCompletedAt(null);

    assertEquals(2L, dto.getLessonId());
    assertEquals("Lesson 2", dto.getTitle());
    assertEquals(2, dto.getOrderNumber());
    assertFalse(dto.isCompleted());
    assertNull(dto.getCompletedAt());
  }
}
