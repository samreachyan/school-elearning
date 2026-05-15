package com.sakcode.elearning.school.features.student.login;

import com.sakcode.elearning.school.features.student.Student;
import com.sakcode.elearning.school.features.student.StudentRepository;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import com.sakcode.elearning.school.shared.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginHandler implements IRequestHandler<LoginRequest, LoginResponse> {

  private final StudentRepository studentRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public LoginResponse handle(LoginRequest request) {
    Student student =
        studentRepository
            .findByEmail(request.getEmail())
            .orElseThrow(
                () -> new BusinessException("Invalid email or password", "INVALID_CREDENTIALS"));

    if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
      throw new BusinessException("Invalid email or password", "INVALID_CREDENTIALS");
    }

    String token = jwtTokenProvider.generateToken(student.getEmail(), student.getId());

    return LoginResponse.builder()
        .token(token)
        .tokenType("Bearer")
        .studentId(student.getId())
        .email(student.getEmail())
        .name(student.getName())
        .planType(student.getPlanType().name())
        .build();
  }
}
