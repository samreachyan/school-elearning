package com.sakcode.elearning.school.features.payment.process;

import com.sakcode.elearning.school.shared.mediator.IRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest implements IRequest<ProcessPaymentResponse> {

  private Long studentId;

  @NotNull(message = "Enrollment ID is required")
  private Long enrollmentId;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be positive")
  private BigDecimal amount;

  @NotBlank(message = "Payment method is required")
  private String paymentMethod;
}
