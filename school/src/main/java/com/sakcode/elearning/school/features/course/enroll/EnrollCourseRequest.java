package com.sakcode.elearning.school.features.course.enroll;

import com.sakcode.elearning.school.shared.mediator.IRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollCourseRequest implements IRequest<EnrollCourseResponse> {

  private Long studentId;

  @NotNull(message = "Course ID is required")
  private Long courseId;
}
