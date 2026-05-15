package com.sakcode.elearning.school.features.student.updateplan;

import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePlanHandler implements IRequestHandler<UpdatePlanRequest, UpdatePlanResponse> {

  private final StudentRepository studentRepository;

  @Override
  public UpdatePlanResponse handle(UpdatePlanRequest request) {
    Student student =
        studentRepository
            .findById(request.getStudentId())
            .orElseThrow(() -> new BusinessException("Student not found", "STUDENT_NOT_FOUND"));

    student.setPlanType(request.getPlanType());
    student = studentRepository.save(student);

    return UpdatePlanResponse.builder()
        .id(student.getId())
        .email(student.getEmail())
        .name(student.getName())
        .planType(student.getPlanType().name())
        .message("Subscription plan updated successfully")
        .build();
  }
}
