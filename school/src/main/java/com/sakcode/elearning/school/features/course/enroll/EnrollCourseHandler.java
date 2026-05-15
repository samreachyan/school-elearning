package com.sakcode.elearning.school.features.course.enroll;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollCourseHandler
    implements IRequestHandler<EnrollCourseRequest, EnrollCourseResponse> {

  private final EnrollmentRepository enrollmentRepository;
  private final CourseRepository courseRepository;

  @Override
  public EnrollCourseResponse handle(EnrollCourseRequest request) {
    Course course =
        courseRepository
            .findById(request.getCourseId())
            .orElseThrow(() -> new BusinessException("Course not found", "COURSE_NOT_FOUND"));

    if (enrollmentRepository.existsByStudentIdAndCourseId(
        request.getStudentId(), request.getCourseId())) {
      throw new BusinessException("Student is already enrolled in this course", "ALREADY_ENROLLED");
    }

    Enrollment enrollment = new Enrollment(request.getStudentId(), request.getCourseId());
    enrollment = enrollmentRepository.save(enrollment);

    return EnrollCourseResponse.builder()
        .enrollmentId(enrollment.getId())
        .studentId(enrollment.getStudentId())
        .courseId(enrollment.getCourseId())
        .enrollmentDate(enrollment.getCreatedAt())
        .message("Successfully enrolled in course")
        .build();
  }
}
