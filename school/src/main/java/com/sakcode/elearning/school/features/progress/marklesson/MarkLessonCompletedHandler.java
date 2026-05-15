package com.sakcode.elearning.school.features.progress.marklesson;

import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.lesson.Lesson;
import com.sakcode.elearning.school.features.lesson.LessonCompletion;
import com.sakcode.elearning.school.features.lesson.LessonCompletionRepository;
import com.sakcode.elearning.school.features.lesson.LessonRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarkLessonCompletedHandler
    implements IRequestHandler<MarkLessonCompletedRequest, MarkLessonCompletedResponse> {

  private final EnrollmentRepository enrollmentRepository;
  private final LessonRepository lessonRepository;
  private final LessonCompletionRepository lessonCompletionRepository;

  @Override
  public MarkLessonCompletedResponse handle(MarkLessonCompletedRequest request) {
    Enrollment enrollment =
        enrollmentRepository
            .findById(request.getEnrollmentId())
            .orElseThrow(
                () -> new BusinessException("Enrollment not found", "ENROLLMENT_NOT_FOUND"));

    Lesson lesson =
        lessonRepository
            .findById(request.getLessonId())
            .orElseThrow(() -> new BusinessException("Lesson not found", "LESSON_NOT_FOUND"));

    if (!lesson.getCourseId().equals(enrollment.getCourseId())) {
      throw new BusinessException(
          "Lesson does not belong to the enrolled course", "INVALID_LESSON");
    }

    if (lessonCompletionRepository.existsByEnrollmentIdAndLessonId(
        request.getEnrollmentId(), request.getLessonId())) {
      throw new BusinessException("Lesson already marked as completed", "LESSON_ALREADY_COMPLETED");
    }

    LessonCompletion completion =
        new LessonCompletion(request.getEnrollmentId(), request.getLessonId());
    completion = lessonCompletionRepository.save(completion);

    // Calculate progress percentage
    long totalLessons = lessonRepository.countByCourseId(enrollment.getCourseId());
    long completedLessons =
        lessonCompletionRepository.countByEnrollmentId(request.getEnrollmentId());

    BigDecimal progress = BigDecimal.ZERO;
    if (totalLessons > 0) {
      progress =
          BigDecimal.valueOf(completedLessons)
              .multiply(BigDecimal.valueOf(100))
              .divide(BigDecimal.valueOf(totalLessons), 2, RoundingMode.HALF_UP);
    }

    enrollment.setProgressPercentage(progress);
    enrollmentRepository.save(enrollment);

    return MarkLessonCompletedResponse.builder()
        .lessonCompletionId(completion.getId())
        .enrollmentId(completion.getEnrollmentId())
        .lessonId(completion.getLessonId())
        .completedAt(completion.getCompletedAt())
        .progressPercentage(progress)
        .message("Lesson marked as completed")
        .build();
  }
}
