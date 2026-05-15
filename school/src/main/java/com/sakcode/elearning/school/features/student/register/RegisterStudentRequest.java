package com.sakcode.elearning.school.features.student.register;

import com.sakcode.elearning.school.features.student.PlanType;
import com.sakcode.elearning.school.shared.mediator.IRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStudentRequest implements IRequest<RegisterStudentResponse> {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Password is required")
  private String password;

  @NotNull(message = "Plan type is required")
  private PlanType planType;
}
