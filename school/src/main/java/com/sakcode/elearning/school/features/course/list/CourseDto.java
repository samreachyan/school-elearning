package com.sakcode.elearning.school.features.course.list;

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
public class CourseDto {

  private Long id;
  private String title;
  private String description;
  private BigDecimal price;
  private String instructor;
  private LocalDateTime createdAt;
}
