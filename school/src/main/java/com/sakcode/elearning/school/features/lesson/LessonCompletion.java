package com.sakcode.elearning.school.features.lesson;

import com.sakcode.elearning.school.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lesson_completions")
@Getter
@Setter
@NoArgsConstructor
public class LessonCompletion extends BaseEntity {

  @Column(nullable = false)
  private Long enrollmentId;

  @Column(nullable = false)
  private Long lessonId;

  private LocalDateTime completedAt;

  public LessonCompletion(Long enrollmentId, Long lessonId) {
    this.enrollmentId = enrollmentId;
    this.lessonId = lessonId;
    this.completedAt = LocalDateTime.now();
  }
}
