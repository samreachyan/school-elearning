package com.sakcode.elearning.school.features.progress.marklesson;

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
public class MarkLessonCompletedRequest implements IRequest<MarkLessonCompletedResponse> {

  @NotNull(message = "Enrollment ID is required")
  private Long enrollmentId;

  @NotNull(message = "Lesson ID is required")
  private Long lessonId;
}
