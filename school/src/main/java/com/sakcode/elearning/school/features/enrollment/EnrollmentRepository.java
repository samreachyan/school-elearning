package com.sakcode.elearning.school.features.enrollment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

  List<Enrollment> findByStudentId(Long studentId);

  boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
