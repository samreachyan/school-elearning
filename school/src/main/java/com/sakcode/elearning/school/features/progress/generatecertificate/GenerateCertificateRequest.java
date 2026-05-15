package com.sakcode.elearning.school.features.progress.generatecertificate;

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
public class GenerateCertificateRequest implements IRequest<GenerateCertificateResponse> {

  @NotNull(message = "Enrollment ID is required")
  private Long enrollmentId;
}
