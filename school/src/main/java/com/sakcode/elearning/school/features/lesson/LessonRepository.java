package com.sakcode.elearning.school.features.lesson;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

  List<Lesson> findByCourseIdOrderByOrderNumberAsc(Long courseId);

  long countByCourseId(Long courseId);
}
