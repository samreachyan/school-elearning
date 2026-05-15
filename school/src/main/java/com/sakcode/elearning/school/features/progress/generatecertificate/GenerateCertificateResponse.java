package com.sakcode.elearning.school.features.progress.generatecertificate;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateCertificateResponse {

  private String certificateId;
  private Long studentId;
  private String studentName;
  private Long courseId;
  private String courseTitle;
  private LocalDateTime completedAt;
  private String message;
}
