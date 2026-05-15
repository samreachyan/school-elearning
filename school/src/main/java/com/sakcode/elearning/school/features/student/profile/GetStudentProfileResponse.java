package com.sakcode.elearning.school.features.student.profile;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStudentProfileResponse {

  private Long id;
  private String email;
  private String name;
  private String planType;
  private LocalDateTime createdAt;
}
