package com.sakcode.elearning.school.features.course.list;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListCoursesHandlerTest {

  @Mock private CourseRepository courseRepository;

  private ListCoursesHandler handler;

  @BeforeEach
  void setUp() {
    handler = new ListCoursesHandler(courseRepository);
  }

  @Test
  void shouldListAllCoursesWhenNoPriceFilter() {
    ListCoursesRequest request = ListCoursesRequest.builder().build();

    Course course1 = new Course("Java", "Java desc", new BigDecimal("49.99"), "Dr. A");
    course1.setId(1L);
    Course course2 = new Course("Python", "Python desc", new BigDecimal("0.00"), "Dr. B");
    course2.setId(2L);

    when(courseRepository.findByPriceRange(null, null)).thenReturn(List.of(course1, course2));

    ListCoursesResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(2, response.getTotalCount());
    assertEquals(2, response.getCourses().size());
    assertEquals("Java", response.getCourses().get(0).getTitle());
    assertEquals("Python", response.getCourses().get(1).getTitle());
    verify(courseRepository).findByPriceRange(null, null);
  }

  @Test
  void shouldFilterCoursesByPriceRange() {
    ListCoursesRequest request =
        ListCoursesRequest.builder()
            .minPrice(new BigDecimal("10"))
            .maxPrice(new BigDecimal("100"))
            .build();

    Course course1 = new Course("Java", "Java desc", new BigDecimal("49.99"), "Dr. A");
    course1.setId(1L);

    when(courseRepository.findByPriceRange(new BigDecimal("10"), new BigDecimal("100")))
        .thenReturn(List.of(course1));

    ListCoursesResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(1, response.getTotalCount());
    assertEquals("Java", response.getCourses().get(0).getTitle());
    verify(courseRepository).findByPriceRange(new BigDecimal("10"), new BigDecimal("100"));
  }

  @Test
  void shouldReturnEmptyListWhenNoCoursesMatch() {
    ListCoursesRequest request =
        ListCoursesRequest.builder().minPrice(new BigDecimal("1000")).build();

    when(courseRepository.findByPriceRange(new BigDecimal("1000"), null)).thenReturn(List.of());

    ListCoursesResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(0, response.getTotalCount());
    assertTrue(response.getCourses().isEmpty());
  }
}
