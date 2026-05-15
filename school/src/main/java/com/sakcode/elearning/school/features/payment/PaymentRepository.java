package com.sakcode.elearning.school.features.payment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByEnrollmentId(Long enrollmentId);

  boolean existsByEnrollmentIdAndStatus(Long enrollmentId, PaymentStatus status);
}
