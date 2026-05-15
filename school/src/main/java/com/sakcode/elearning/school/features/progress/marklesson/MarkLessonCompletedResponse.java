package com.sakcode.elearning.school.features.progress.marklesson;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkLessonCompletedResponse {

  private Long lessonCompletionId;
  private Long enrollmentId;
  private Long lessonId;
  private LocalDateTime completedAt;
  private BigDecimal progressPercentage;
  private String message;
}
