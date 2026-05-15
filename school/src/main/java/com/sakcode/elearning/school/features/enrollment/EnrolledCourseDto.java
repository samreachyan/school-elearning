package com.sakcode.elearning.school.features.enrollment;

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
public class EnrolledCourseDto {

  private Long enrollmentId;
  private Long courseId;
  private String courseTitle;
  private String courseDescription;
  private String instructor;
  private BigDecimal price;
  private BigDecimal progressPercentage;
  private PaymentStatus paymentStatus;
  private LocalDateTime enrollmentDate;
}
