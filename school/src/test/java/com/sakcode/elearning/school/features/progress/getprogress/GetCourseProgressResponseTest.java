package com.sakcode.elearning.school.features.progress.getprogress;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class GetCourseProgressResponseTest {

  @Test
  void shouldCreateResponseUsingBuilder() {
    LessonProgressDto dto =
        LessonProgressDto.builder().lessonId(1L).title("Lesson 1").orderNumber(1).completed(true).build();

    GetCourseProgressResponse response =
        GetCourseProgressResponse.builder()
            .enrollmentId(1L)
            .studentId(1L)
            .courseId(100L)
            .courseTitle("Java")
            .progressPercentage(new BigDecimal("50.00"))
            .totalLessons(2)
            .completedLessons(1)
            .lessons(List.of(dto))
            .build();

    assertEquals(1L, response.getEnrollmentId());
    assertEquals(1L, response.getStudentId());
    assertEquals(100L, response.getCourseId());
    assertEquals("Java", response.getCourseTitle());
    assertEquals(0, new BigDecimal("50.00").compareTo(response.getProgressPercentage()));
    assertEquals(2, response.getTotalLessons());
    assertEquals(1, response.getCompletedLessons());
    assertEquals(1, response.getLessons().size());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    GetCourseProgressResponse response = new GetCourseProgressResponse();
    assertNull(response.getEnrollmentId());
    assertNull(response.getStudentId());
    assertNull(response.getCourseId());
    assertNull(response.getCourseTitle());
    assertNull(response.getProgressPercentage());
    assertEquals(0, response.getTotalLessons());
    assertEquals(0, response.getCompletedLessons());
    assertNull(response.getLessons());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    GetCourseProgressResponse response =
        new GetCourseProgressResponse(1L, 1L, 100L, "Java", new BigDecimal("50.00"), 2, 1, List.of());

    assertEquals(1L, response.getEnrollmentId());
    assertEquals("Java", response.getCourseTitle());
    assertEquals(2, response.getTotalLessons());
  }

  @Test
  void shouldSetAndGetFields() {
    GetCourseProgressResponse response = new GetCourseProgressResponse();
    response.setEnrollmentId(5L);
    response.setStudentId(2L);
    response.setCourseId(200L);
    response.setCourseTitle("Python");
    response.setProgressPercentage(new BigDecimal("75.00"));
    response.setTotalLessons(4);
    response.setCompletedLessons(3);
    response.setLessons(List.of());

    assertEquals(5L, response.getEnrollmentId());
    assertEquals(2L, response.getStudentId());
    assertEquals(200L, response.getCourseId());
    assertEquals("Python", response.getCourseTitle());
    assertEquals(0, new BigDecimal("75.00").compareTo(response.getProgressPercentage()));
    assertEquals(4, response.getTotalLessons());
    assertEquals(3, response.getCompletedLessons());
    assertTrue(response.getLessons().isEmpty());
  }
}
