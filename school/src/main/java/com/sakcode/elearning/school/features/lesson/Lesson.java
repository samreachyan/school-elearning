package com.sakcode.elearning.school.features.lesson;

import com.sakcode.elearning.school.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
public class Lesson extends BaseEntity {

  @Column(nullable = false)
  private Long courseId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private Integer orderNumber;

  public Lesson(Long courseId, String title, String content, Integer orderNumber) {
    this.courseId = courseId;
    this.title = title;
    this.content = content;
    this.orderNumber = orderNumber;
  }
}
