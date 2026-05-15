package com.sakcode.elearning.school.features.enrollment;

import com.sakcode.elearning.school.features.payment.PaymentStatus;
import com.sakcode.elearning.school.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
public class Enrollment extends BaseEntity {

  @Column(nullable = false)
  private Long studentId;

  @Column(nullable = false)
  private Long courseId;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal progressPercentage;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus paymentStatus;

  public Enrollment(Long studentId, Long courseId) {
    this.studentId = studentId;
    this.courseId = courseId;
    this.progressPercentage = BigDecimal.ZERO;
    this.paymentStatus = PaymentStatus.PENDING;
  }
}
