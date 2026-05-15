package com.sakcode.elearning.school.features.enrollment;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrolledCourseController {

  private final EnrollmentRepository enrollmentRepository;
  private final CourseRepository courseRepository;

  @GetMapping
  public ResponseEntity<List<EnrolledCourseDto>> getEnrolledCourses(
      @AuthenticationPrincipal StudentPrincipal principal) {
    List<Enrollment> enrollments = enrollmentRepository.findByStudentId(principal.getStudentId());

    List<EnrolledCourseDto> enrolledCourses =
        enrollments.stream()
            .map(
                enrollment -> {
                  Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
                  return EnrolledCourseDto.builder()
                      .enrollmentId(enrollment.getId())
                      .courseId(enrollment.getCourseId())
                      .courseTitle(course != null ? course.getTitle() : "Unknown")
                      .courseDescription(course != null ? course.getDescription() : "")
                      .instructor(course != null ? course.getInstructor() : "")
                      .price(course != null ? course.getPrice() : null)
                      .progressPercentage(enrollment.getProgressPercentage())
                      .paymentStatus(enrollment.getPaymentStatus())
                      .enrollmentDate(enrollment.getCreatedAt())
                      .build();
                })
            .collect(Collectors.toList());

    return ResponseEntity.ok(enrolledCourses);
  }
}
