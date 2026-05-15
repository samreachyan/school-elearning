package com.sakcode.elearning.school.features.student.profile;

import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetStudentProfileHandler
    implements IRequestHandler<GetStudentProfileRequest, GetStudentProfileResponse> {

  private final StudentRepository studentRepository;

  @Override
  public GetStudentProfileResponse handle(GetStudentProfileRequest request) {
    Student student =
        studentRepository
            .findById(request.getStudentId())
            .orElseThrow(() -> new BusinessException("Student not found", "STUDENT_NOT_FOUND"));

    return GetStudentProfileResponse.builder()
        .id(student.getId())
        .email(student.getEmail())
        .name(student.getName())
        .planType(student.getPlanType().name())
        .createdAt(student.getCreatedAt())
        .build();
  }
}
