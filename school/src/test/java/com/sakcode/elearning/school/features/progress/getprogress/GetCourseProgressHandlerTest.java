package com.sakcode.elearning.school.features.progress.getprogress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.lesson.Lesson;
import com.sakcode.elearning.school.features.lesson.LessonCompletion;
import com.sakcode.elearning.school.features.lesson.LessonCompletionRepository;
import com.sakcode.elearning.school.features.lesson.LessonRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCourseProgressHandlerTest {

  @Mock private EnrollmentRepository enrollmentRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private LessonRepository lessonRepository;
  @Mock private LessonCompletionRepository lessonCompletionRepository;

  private GetCourseProgressHandler handler;

  @BeforeEach
  void setUp() {
    handler =
        new GetCourseProgressHandler(
            enrollmentRepository, courseRepository, lessonRepository, lessonCompletionRepository);
  }

  @Test
  void shouldGetCourseProgressSuccessfully() {
    GetCourseProgressRequest request = GetCourseProgressRequest.builder().enrollmentId(1L).build();

    Enrollment enrollment = new Enrollment(1L, 100L);
    enrollment.setId(1L);
    enrollment.setProgressPercentage(new BigDecimal("40.00"));

    Course course = new Course("Java", "Java course", new BigDecimal("49.99"), "Dr. Smith");
    course.setId(100L);

    Lesson lesson1 = new Lesson(100L, "Lesson 1", "Content 1", 1);
    lesson1.setId(10L);
    Lesson lesson2 = new Lesson(100L, "Lesson 2", "Content 2", 2);
    lesson2.setId(20L);

    LessonCompletion completion1 = new LessonCompletion(1L, 10L);
    completion1.setCompletedAt(LocalDateTime.now());

    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(courseRepository.findById(100L)).thenReturn(Optional.of(course));
    when(lessonRepository.findByCourseIdOrderByOrderNumberAsc(100L))
        .thenReturn(List.of(lesson1, lesson2));
    when(lessonCompletionRepository.findAll()).thenReturn(List.of(completion1));

    GetCourseProgressResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(1L, response.getEnrollmentId());
    assertEquals(1L, response.getStudentId());
    assertEquals(100L, response.getCourseId());
    assertEquals("Java", response.getCourseTitle());
    assertEquals(0, new BigDecimal("40.00").compareTo(response.getProgressPercentage()));
    assertEquals(2, response.getTotalLessons());
    assertEquals(1, response.getCompletedLessons());
    assertEquals(2, response.getLessons().size());

    LessonProgressDto dto1 = response.getLessons().get(0);
    assertEquals(10L, dto1.getLessonId());
    assertEquals("Lesson 1", dto1.getTitle());
    assertEquals(1, dto1.getOrderNumber());
    assertTrue(dto1.isCompleted());
    assertNotNull(dto1.getCompletedAt());

    LessonProgressDto dto2 = response.getLessons().get(1);
    assertEquals(20L, dto2.getLessonId());
    assertEquals("Lesson 2", dto2.getTitle());
    assertEquals(2, dto2.getOrderNumber());
    assertFalse(dto2.isCompleted());
    assertNull(dto2.getCompletedAt());
  }

  @Test
  void shouldThrowExceptionWhenEnrollmentNotFound() {
    GetCourseProgressRequest request =
        GetCourseProgressRequest.builder().enrollmentId(999L).build();

    when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Enrollment not found", exception.getMessage());
    assertEquals("ENROLLMENT_NOT_FOUND", exception.getErrorCode());
  }

  @Test
  void shouldThrowExceptionWhenCourseNotFound() {
    GetCourseProgressRequest request = GetCourseProgressRequest.builder().enrollmentId(1L).build();

    Enrollment enrollment = new Enrollment(1L, 999L);
    enrollment.setId(1L);
    when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
    when(courseRepository.findById(999L)).thenReturn(Optional.empty());

    BusinessException exception =
        assertThrows(BusinessException.class, () -> handler.handle(request));
    assertEquals("Course not found", exception.getMessage());
    assertEquals("COURSE_NOT_FOUND", exception.getErrorCode());
  }
}
