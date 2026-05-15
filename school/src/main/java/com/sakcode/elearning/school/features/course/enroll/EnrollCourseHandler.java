package com.sakcode.elearning.school.features.course.enroll;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.payment.PaymentStatus;
import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollCourseHandler
    implements IRequestHandler<EnrollCourseRequest, EnrollCourseResponse> {

  private final EnrollmentRepository enrollmentRepository;
  private final CourseRepository courseRepository;
  private final StudentRepository studentRepository;

  @Override
  public EnrollCourseResponse handle(EnrollCourseRequest request) {
    Course course =
        courseRepository
            .findById(request.getCourseId())
            .orElseThrow(() -> new BusinessException("Course not found", "COURSE_NOT_FOUND"));

    Student student =
        studentRepository
            .findById(request.getStudentId())
            .orElseThrow(() -> new BusinessException("Student not found", "STUDENT_NOT_FOUND"));

    if (enrollmentRepository.existsByStudentIdAndCourseId(
        request.getStudentId(), request.getCourseId())) {
      throw new BusinessException("Student is already enrolled in this course", "ALREADY_ENROLLED");
    }

    // Determine payment status based on course price and student plan
    PaymentStatus paymentStatus = resolvePaymentStatus(course.getPrice(), student.getPlanType());

    Enrollment enrollment = new Enrollment(request.getStudentId(), request.getCourseId());
    enrollment.setPaymentStatus(paymentStatus);
    enrollment = enrollmentRepository.save(enrollment);

    return EnrollCourseResponse.builder()
        .enrollmentId(enrollment.getId())
        .studentId(enrollment.getStudentId())
        .courseId(enrollment.getCourseId())
        .enrollmentDate(enrollment.getCreatedAt())
        .paymentStatus(paymentStatus)
        .amount(course.getPrice())
        .message(buildMessage(paymentStatus, course.getPrice()))
        .build();
  }

  private PaymentStatus resolvePaymentStatus(BigDecimal price, PlanType planType) {
    // Free courses are always available
    if (price.compareTo(BigDecimal.ZERO) == 0) {
      return PaymentStatus.COMPLETED;
    }

    // PREMIUM plan students get all courses for free
    if (planType == PlanType.PREMIUM) {
      return PaymentStatus.COMPLETED;
    }

    // FREE plan students need to pay for paid courses
    return PaymentStatus.PENDING;
  }

  private String buildMessage(PaymentStatus paymentStatus, BigDecimal price) {
    if (paymentStatus == PaymentStatus.COMPLETED) {
      if (price.compareTo(BigDecimal.ZERO) == 0) {
        return "Successfully enrolled in free course";
      }
      return "Successfully enrolled in course (PREMIUM plan benefit)";
    }
    return "Successfully enrolled. Payment of $" + price + " is required to access course content";
  }
}
