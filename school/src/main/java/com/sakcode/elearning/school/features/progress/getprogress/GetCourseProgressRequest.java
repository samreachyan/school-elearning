package com.sakcode.elearning.school.features.progress.getprogress;

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
public class GetCourseProgressRequest implements IRequest<GetCourseProgressResponse> {

  @NotNull(message = "Enrollment ID is required")
  private Long enrollmentId;
}
