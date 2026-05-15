package com.sakcode.elearning.school.features.progress.getprogress;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgressDto {

  private Long lessonId;
  private String title;
  private int orderNumber;
  private boolean completed;
  private LocalDateTime completedAt;
}
