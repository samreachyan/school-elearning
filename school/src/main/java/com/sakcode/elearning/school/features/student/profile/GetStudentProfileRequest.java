package com.sakcode.elearning.school.features.student.profile;

import com.sakcode.elearning.school.shared.mediator.IRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStudentProfileRequest implements IRequest<GetStudentProfileResponse> {

  private Long studentId;
}
