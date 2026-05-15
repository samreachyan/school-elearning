package com.sakcode.elearning.school.features.progress.generatecertificate;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateCertificateHandler
    implements IRequestHandler<GenerateCertificateRequest, GenerateCertificateResponse> {

  private final EnrollmentRepository enrollmentRepository;
  private final CourseRepository courseRepository;
  private final StudentRepository studentRepository;

  @Override
  public GenerateCertificateResponse handle(GenerateCertificateRequest request) {
    Enrollment enrollment =
        enrollmentRepository
            .findById(request.getEnrollmentId())
            .orElseThrow(
                () -> new BusinessException("Enrollment not found", "ENROLLMENT_NOT_FOUND"));

    if (enrollment.getProgressPercentage().compareTo(BigDecimal.valueOf(100)) < 0) {
      throw new BusinessException(
          "Course not yet completed. Progress: " + enrollment.getProgressPercentage() + "%",
          "COURSE_NOT_COMPLETED");
    }

    Course course =
        courseRepository
            .findById(enrollment.getCourseId())
            .orElseThrow(() -> new BusinessException("Course not found", "COURSE_NOT_FOUND"));

    Student student =
        studentRepository
            .findById(enrollment.getStudentId())
            .orElseThrow(() -> new BusinessException("Student not found", "STUDENT_NOT_FOUND"));

    String certificateId = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    return GenerateCertificateResponse.builder()
        .certificateId(certificateId)
        .studentId(student.getId())
        .studentName(student.getName())
        .courseId(course.getId())
        .courseTitle(course.getTitle())
        .completedAt(LocalDateTime.now())
        .message("Certificate generated successfully")
        .build();
  }
}
