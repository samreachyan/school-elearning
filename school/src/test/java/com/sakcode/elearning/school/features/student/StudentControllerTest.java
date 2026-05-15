package com.sakcode.elearning.school.features.student;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.student.login.LoginRequest;
import com.sakcode.elearning.school.features.student.login.LoginResponse;
import com.sakcode.elearning.school.features.student.profile.GetStudentProfileResponse;
import com.sakcode.elearning.school.features.student.register.RegisterStudentRequest;
import com.sakcode.elearning.school.features.student.register.RegisterStudentResponse;
import com.sakcode.elearning.school.features.student.updateplan.UpdatePlanRequest;
import com.sakcode.elearning.school.features.student.updateplan.UpdatePlanResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

  @Mock private Mediator mediator;

  private StudentController controller;

  @BeforeEach
  void setUp() {
    controller = new StudentController(mediator);
  }

  @Test
  void shouldRegisterStudent() {
    RegisterStudentRequest request =
        RegisterStudentRequest.builder()
            .email("john@example.com")
            .name("John Doe")
            .password("password")
            .planType(PlanType.FREE)
            .build();

    RegisterStudentResponse expectedResponse =
        RegisterStudentResponse.builder()
            .id(1L)
            .email("john@example.com")
            .name("John Doe")
            .planType("FREE")
            .message("Student registered successfully")
            .build();

    when(mediator.send(request)).thenReturn(expectedResponse);

    ResponseEntity<RegisterStudentResponse> response = controller.register(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    RegisterStudentResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(1L, body.getId());
    assertEquals("john@example.com", body.getEmail());
  }

  @Test
  void shouldLogin() {
    LoginRequest request =
        LoginRequest.builder().email("john@example.com").password("password").build();

    LoginResponse expectedResponse =
        LoginResponse.builder()
            .token("jwt-token")
            .tokenType("Bearer")
            .studentId(1L)
            .email("john@example.com")
            .name("John Doe")
            .planType("FREE")
            .build();

    when(mediator.send(request)).thenReturn(expectedResponse);

    ResponseEntity<LoginResponse> response = controller.login(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    LoginResponse body = response.getBody();
    assertNotNull(body);
    assertEquals("jwt-token", body.getToken());
    assertEquals("john@example.com", body.getEmail());
  }

  @Test
  void shouldGetProfile() {
    StudentPrincipal principal = new StudentPrincipal("john@example.com", 1L);

    GetStudentProfileResponse expectedResponse =
        GetStudentProfileResponse.builder()
            .id(1L)
            .email("john@example.com")
            .name("John Doe")
            .planType("FREE")
            .createdAt(LocalDateTime.now())
            .build();

    when(mediator.send(any())).thenReturn(expectedResponse);

    ResponseEntity<GetStudentProfileResponse> response = controller.profile(principal);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    GetStudentProfileResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(1L, body.getId());
    assertEquals("John Doe", body.getName());
  }

  @Test
  void shouldUpdatePlan() {
    StudentPrincipal principal = new StudentPrincipal("john@example.com", 1L);
    UpdatePlanRequest request = UpdatePlanRequest.builder().planType(PlanType.PREMIUM).build();

    UpdatePlanResponse expectedResponse =
        UpdatePlanResponse.builder()
            .id(1L)
            .email("john@example.com")
            .name("John Doe")
            .planType("PREMIUM")
            .message("Subscription plan updated successfully")
            .build();

    when(mediator.send(request)).thenReturn(expectedResponse);

    ResponseEntity<UpdatePlanResponse> response = controller.updatePlan(principal, request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    UpdatePlanResponse body = response.getBody();
    assertNotNull(body);
    assertEquals("PREMIUM", body.getPlanType());
    assertEquals(1L, request.getStudentId().longValue());
  }
}
