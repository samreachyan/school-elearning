package com.sakcode.elearning.school.features.course.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCourseHandlerTest {

  @Mock private CourseRepository courseRepository;

  private CreateCourseHandler handler;

  @BeforeEach
  void setUp() {
    handler = new CreateCourseHandler(courseRepository);
  }

  @Test
  void shouldCreateCourseSuccessfully() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Java Basics")
            .description("Learn Java")
            .price(new BigDecimal("49.99"))
            .instructor("Dr. Smith")
            .build();

    Course savedCourse = new Course("Java Basics", "Learn Java", new BigDecimal("49.99"), "Dr. Smith");
    savedCourse.setId(1L);
    savedCourse.setCreatedAt(LocalDateTime.now());
    when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

    CreateCourseResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(1L, response.getId());
    assertEquals("Java Basics", response.getTitle());
    assertEquals("Learn Java", response.getDescription());
    assertEquals(0, new BigDecimal("49.99").compareTo(response.getPrice()));
    assertEquals("Dr. Smith", response.getInstructor());
    assertEquals("Course created successfully", response.getMessage());
    assertNotNull(response.getCreatedAt());
    verify(courseRepository).save(any(Course.class));
  }
}
