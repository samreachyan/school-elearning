package com.sakcode.elearning.school.features.student.updateplan;

import com.sakcode.elearning.school.features.student.PlanType;
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
public class UpdatePlanRequest implements IRequest<UpdatePlanResponse> {

  @NotNull(message = "Student ID is required")
  private Long studentId;

  @NotNull(message = "Plan type is required")
  private PlanType planType;
}
