package com.sakcode.elearning.school.features.lesson;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCompletionRepository extends JpaRepository<LessonCompletion, Long> {

  boolean existsByEnrollmentIdAndLessonId(Long enrollmentId, Long lessonId);

  long countByEnrollmentId(Long enrollmentId);

  Optional<LessonCompletion> findByEnrollmentIdAndLessonId(Long enrollmentId, Long lessonId);
}
