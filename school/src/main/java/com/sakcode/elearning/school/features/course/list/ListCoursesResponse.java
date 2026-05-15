package com.sakcode.elearning.school.features.course.list;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListCoursesResponse {

  private List<CourseDto> courses;
  private int totalCount;
}
