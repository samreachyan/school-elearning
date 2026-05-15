package com.sakcode.elearning.school.features.student;

import com.sakcode.elearning.school.features.student.login.LoginRequest;
import com.sakcode.elearning.school.features.student.login.LoginResponse;
import com.sakcode.elearning.school.features.student.profile.GetStudentProfileRequest;
import com.sakcode.elearning.school.features.student.profile.GetStudentProfileResponse;
import com.sakcode.elearning.school.features.student.register.RegisterStudentRequest;
import com.sakcode.elearning.school.features.student.register.RegisterStudentResponse;
import com.sakcode.elearning.school.features.student.updateplan.UpdatePlanRequest;
import com.sakcode.elearning.school.features.student.updateplan.UpdatePlanResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class StudentController {

  private final Mediator mediator;

  @PostMapping("/register")
  public ResponseEntity<RegisterStudentResponse> register(
      @Valid @RequestBody RegisterStudentRequest request) {
    RegisterStudentResponse response = mediator.send(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = mediator.send(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/profile")
  public ResponseEntity<GetStudentProfileResponse> profile(
      @AuthenticationPrincipal StudentPrincipal principal) {
    GetStudentProfileRequest request =
        GetStudentProfileRequest.builder().studentId(principal.getStudentId()).build();
    GetStudentProfileResponse response = mediator.send(request);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/plan")
  public ResponseEntity<UpdatePlanResponse> updatePlan(
      @AuthenticationPrincipal StudentPrincipal principal,
      @Valid @RequestBody UpdatePlanRequest request) {
    request.setStudentId(principal.getStudentId());
    UpdatePlanResponse response = mediator.send(request);
    return ResponseEntity.ok(response);
  }
}
