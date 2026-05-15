package com.sakcode.elearning.school.features.student.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private String token;
  private String tokenType;
  private Long studentId;
  private String email;
  private String name;
  private String planType;
}
