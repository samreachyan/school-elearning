package com.sakcode.elearning.school.features.student.register;

import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterStudentHandler
    implements IRequestHandler<RegisterStudentRequest, RegisterStudentResponse> {

  private final StudentRepository studentRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public RegisterStudentResponse handle(RegisterStudentRequest request) {
    if (studentRepository.existsByEmail(request.getEmail())) {
      throw new BusinessException("Email already registered", "EMAIL_EXISTS");
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    Student student =
        new Student(request.getEmail(), request.getName(), encodedPassword, request.getPlanType());

    student = studentRepository.save(student);

    return RegisterStudentResponse.builder()
        .id(student.getId())
        .email(student.getEmail())
        .name(student.getName())
        .planType(student.getPlanType().name())
        .message("Student registered successfully")
        .build();
  }
}
