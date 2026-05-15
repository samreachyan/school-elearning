package com.sakcode.elearning.school.features.payment.process;

import com.sakcode.elearning.school.features.payment.PaymentStatus;
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
public class ProcessPaymentResponse {

  private Long paymentId;
  private Long enrollmentId;
  private BigDecimal amount;
  private String paymentMethod;
  private PaymentStatus status;
  private String transactionId;
  private LocalDateTime paidAt;
  private String message;
}
