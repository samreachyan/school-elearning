package com.sakcode.elearning.school.features.payment.process;

import com.sakcode.elearning.school.features.enrollment.Enrollment;
import com.sakcode.elearning.school.features.enrollment.EnrollmentRepository;
import com.sakcode.elearning.school.features.payment.Payment;
import com.sakcode.elearning.school.features.payment.PaymentRepository;
import com.sakcode.elearning.school.features.payment.PaymentStatus;
import com.sakcode.elearning.school.shared.exception.BusinessException;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProcessPaymentHandler
    implements IRequestHandler<ProcessPaymentRequest, ProcessPaymentResponse> {

  private final PaymentRepository paymentRepository;
  private final EnrollmentRepository enrollmentRepository;

  @Override
  @Transactional
  public ProcessPaymentResponse handle(ProcessPaymentRequest request) {
    Enrollment enrollment =
        enrollmentRepository
            .findById(request.getEnrollmentId())
            .orElseThrow(
                () -> new BusinessException("Enrollment not found", "ENROLLMENT_NOT_FOUND"));

    if (!enrollment.getStudentId().equals(request.getStudentId())) {
      throw new BusinessException(
          "Enrollment does not belong to this student", "INVALID_ENROLLMENT");
    }

    if (enrollment.getPaymentStatus() == PaymentStatus.COMPLETED) {
      throw new BusinessException(
          "Payment already completed for this enrollment", "PAYMENT_ALREADY_COMPLETED");
    }

    // Simulate payment processing (in real life, this would call a payment gateway)
    Payment payment =
        new Payment(
            request.getEnrollmentId(),
            request.getStudentId(),
            request.getAmount(),
            request.getPaymentMethod());

    // Simulate successful payment
    payment.setStatus(PaymentStatus.COMPLETED);
    payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    payment.setPaidAt(LocalDateTime.now());

    payment = paymentRepository.save(payment);

    // Update enrollment payment status
    enrollment.setPaymentStatus(PaymentStatus.COMPLETED);
    enrollmentRepository.save(enrollment);

    return ProcessPaymentResponse.builder()
        .paymentId(payment.getId())
        .enrollmentId(payment.getEnrollmentId())
        .amount(payment.getAmount())
        .paymentMethod(payment.getPaymentMethod())
        .status(payment.getStatus())
        .transactionId(payment.getTransactionId())
        .paidAt(payment.getPaidAt())
        .message("Payment processed successfully")
        .build();
  }
}
