package com.sakcode.elearning.school.features.student.updateplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanResponse {

  private Long id;
  private String email;
  private String name;
  private String planType;
  private String message;
}
