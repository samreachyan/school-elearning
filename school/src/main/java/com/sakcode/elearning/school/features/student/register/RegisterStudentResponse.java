package com.sakcode.elearning.school.features.student.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStudentResponse {

  private Long id;
  private String email;
  private String name;
  private String planType;
  private String message;
}
