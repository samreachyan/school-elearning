package com.sakcode.elearning.school.features.progress.marklesson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.lesson.Lesson;
import com.sakcode.elearning.school.features.lesson.LessonCompletion;
import com.sakcode.elearning.school.features.lesson.LessonCompletionRepository;
import com.sakcode.elearning.school.features.lesson.LessonRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarkLessonCompletedHandlerTest {

  @Mock private EnrollmentRepository enrollmentRepository;
  @Mock private LessonRepository lessonRepository;
  @Mock private LessonCompletionRepository lessonCompletionRepository;

  private MarkLessonCompletedHandler handler;

  @BeforeEach
  void setUp() {
    handler =
        new MarkLessonCompletedHandler(
            enrollmentRepository, lessonRepository, lessonCompletionRepository);
  }

  @Test
  void shouldMarkLessonCompletedSuccessfully() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(1L).lessonId(10L).build();

    Enrollment enrollment = new Enrollment(1L, 100L);
    enrollment.setId(1L);
    Lesson lesson = new Lesson(100L, "Lesson 1", "Content", 1);
    lesson.setId(10L);

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(lessonRepository.findById(10L)).thenReturn(Optional.of(lesson));
    when(lessonCompletionRepository.existsByEnrollmentIdAndLessonId(1L, 10L)).thenReturn(false);

    LessonCompletion savedCompletion = new LessonCompletion(1L, 10L);
    savedCompletion.setId(50L);
    when(lessonCompletionRepository.save(any(LessonCompletion.class))).thenReturn(savedCompletion);
    when(lessonRepository.countByCourseId(100L)).thenReturn(5L);
    when(lessonCompletionRepository.countByEnrollmentId(1L)).thenReturn(1L);

    MarkLessonCompletedResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(50L, response.getLessonCompletionId());
    assertEquals(1L, response.getEnrollmentId());
    assertEquals(10L, response.getLessonId());
    assertEquals("Lesson marked as completed", response.getMessage());
    assertNotNull(response.getCompletedAt());
    assertEquals(0, new BigDecimal("20.00").compareTo(response.getProgressPercentage()));
    verify(enrollmentRepository).save(enrollment);
  }

  @Test
  void shouldThrowExceptionWhenEnrollmentNotFound() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(999L).lessonId(10L).build();

    when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Enrollment not found", exception.getMessage());
    assertEquals("ENROLLMENT_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenLessonNotFound() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(1L).lessonId(999L).build();

    Enrollment enrollment = new Enrollment(1L, 100L);
    enrollment.setId(1L);
    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(lessonRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Lesson not found", exception.getMessage());
    assertEquals("LESSON_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenLessonDoesNotBelongToCourse() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(1L).lessonId(10L).build();

    Enrollment enrollment = new Enrollment(1L, 100L);
    enrollment.setId(1L);
    Lesson lesson = new Lesson(200L, "Lesson", "Content", 1);
    lesson.setId(10L);

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(lessonRepository.findById(10L)).thenReturn(Optional.of(lesson));

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Lesson does not belong to the enrolled course", exception.getMessage());
    assertEquals("INVALID_LESSON", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenLessonAlreadyCompleted() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(1L).lessonId(10L).build();

    Enrollment enrollment = new Enrollment(1L, 100L);
    enrollment.setId(1L);
    Lesson lesson = new Lesson(100L, "Lesson", "Content", 1);
    lesson.setId(10L);

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(lessonRepository.findById(10L)).thenReturn(Optional.of(lesson));
    when(lessonCompletionRepository.existsByEnrollmentIdAndLessonId(1L, 10L)).thenReturn(true);

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Lesson already marked as completed", exception.getMessage());
    assertEquals("LESSON_ALREADY_COMPLETED", exception.getErrorCode());
  }
}
