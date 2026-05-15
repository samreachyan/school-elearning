package com.sakcode.elearning.school.features.progress.getprogress;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.lesson.Lesson;
import com.sakcode.elearning.school.features.lesson.LessonCompletion;
import com.sakcode.elearning.school.features.lesson.LessonCompletionRepository;
import com.sakcode.elearning.school.features.lesson.LessonRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCourseProgressHandler
    implements IRequestHandler<GetCourseProgressRequest, GetCourseProgressResponse> {

  private final EnrollmentRepository enrollmentRepository;
  private final CourseRepository courseRepository;
  private final LessonRepository lessonRepository;
  private final LessonCompletionRepository lessonCompletionRepository;

  @Override
  public GetCourseProgressResponse handle(GetCourseProgressRequest request) {
    Enrollment enrollment =
        enrollmentRepository
            .findById(request.getEnrollmentId())
            .orElseThrow(
                () -> new BusinessException("Enrollment not found", "ENROLLMENT_NOT_FOUND"));

    Course course =
        courseRepository
            .findById(enrollment.getCourseId())
            .orElseThrow(() -> new BusinessException("Course not found", "COURSE_NOT_FOUND"));

    List<Lesson> lessons =
        lessonRepository.findByCourseIdOrderByOrderNumberAsc(enrollment.getCourseId());

    Map<Long, LessonCompletion> completionMap =
        lessonCompletionRepository.findAll().stream()
            .filter(lc -> lc.getEnrollmentId().equals(request.getEnrollmentId()))
            .collect(Collectors.toMap(LessonCompletion::getLessonId, lc -> lc, (a, b) -> a));

    List<LessonProgressDto> lessonDtos =
        lessons.stream()
            .map(
                lesson -> {
                  LessonCompletion completion = completionMap.get(lesson.getId());
                  return LessonProgressDto.builder()
                      .lessonId(lesson.getId())
                      .title(lesson.getTitle())
                      .orderNumber(lesson.getOrderNumber())
                      .completed(completion != null)
                      .completedAt(completion != null ? completion.getCompletedAt() : null)
                      .build();
                })
            .collect(Collectors.toList());

    long completedLessons = lessonDtos.stream().filter(LessonProgressDto::isCompleted).count();

    return GetCourseProgressResponse.builder()
        .enrollmentId(enrollment.getId())
        .studentId(enrollment.getStudentId())
        .courseId(course.getId())
        .courseTitle(course.getTitle())
        .progressPercentage(enrollment.getProgressPercentage())
        .totalLessons(lessons.size())
        .completedLessons(completedLessons)
        .lessons(lessonDtos)
        .build();
  }
}
