package com.sakcode.elearning.school.features.payment;

import com.sakcode.elearning.school.features.payment.process.ProcessPaymentRequest;
import com.sakcode.elearning.school.features.payment.process.ProcessPaymentResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final Mediator mediator;

  @PostMapping
  public ResponseEntity<ProcessPaymentResponse> processPayment(
      @AuthenticationPrincipal StudentPrincipal principal,
      @Valid @RequestBody ProcessPaymentRequest request) {
    request.setStudentId(principal.getStudentId());
    ProcessPaymentResponse response = mediator.send(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
