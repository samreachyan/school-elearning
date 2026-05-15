package com.sakcode.elearning.school.features.payment;

import com.sakcode.elearning.school.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseEntity {

  @Column(nullable = false)
  private Long enrollmentId;

  @Column(nullable = false)
  private Long studentId;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false)
  private String paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

  @Column(unique = true)
  private String transactionId;

  private LocalDateTime paidAt;

  public Payment(Long enrollmentId, Long studentId, BigDecimal amount, String paymentMethod) {
    this.enrollmentId = enrollmentId;
    this.studentId = studentId;
    this.amount = amount;
    this.paymentMethod = paymentMethod;
    this.status = PaymentStatus.PENDING;
  }
}
