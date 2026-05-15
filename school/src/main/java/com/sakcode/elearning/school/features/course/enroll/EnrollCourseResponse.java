package com.sakcode.elearning.school.features.course.enroll;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollCourseResponse {

  private Long enrollmentId;
  private Long studentId;
  private Long courseId;
  private LocalDateTime enrollmentDate;
  private String message;
}
