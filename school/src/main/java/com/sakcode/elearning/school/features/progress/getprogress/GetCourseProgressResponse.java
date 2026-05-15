package com.sakcode.elearning.school.features.progress.getprogress;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCourseProgressResponse {

  private Long enrollmentId;
  private Long studentId;
  private Long courseId;
  private String courseTitle;
  private BigDecimal progressPercentage;
  private long totalLessons;
  private long completedLessons;
  private List<LessonProgressDto> lessons;
}
