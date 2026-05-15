package com.sakcode.elearning.school.features.course;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.course.create.CreateCourseRequest;
import com.sakcode.elearning.school.features.course.create.CreateCourseResponse;
import com.sakcode.elearning.school.features.course.enroll.EnrollCourseRequest;
import com.sakcode.elearning.school.features.course.enroll.EnrollCourseResponse;
import com.sakcode.elearning.school.features.course.list.ListCoursesResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

  @Mock private Mediator mediator;

  private CourseController controller;

  @BeforeEach
  void setUp() {
    controller = new CourseController(mediator);
  }

  @Test
  void shouldCreateCourse() {
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Java")
            .description("Java course")
            .price(new BigDecimal("49.99"))
            .instructor("Dr. Smith")
            .build();

    CreateCourseResponse expectedResponse =
        CreateCourseResponse.builder()
            .id(1L)
            .title("Java")
            .description("Java course")
            .price(new BigDecimal("49.99"))
            .instructor("Dr. Smith")
            .createdAt(LocalDateTime.now())
            .message("Course created successfully")
            .build();

    when(mediator.send(request)).thenReturn(expectedResponse);

    ResponseEntity<CreateCourseResponse> response = controller.createCourse(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    CreateCourseResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(1L, body.getId());
    assertEquals("Java", body.getTitle());
  }

  @Test
  void shouldListCourses() {
    ListCoursesResponse expectedResponse =
        ListCoursesResponse.builder().courses(List.of()).totalCount(0).build();

    when(mediator.send(any())).thenReturn(expectedResponse);

    ResponseEntity<ListCoursesResponse> response = controller.listCourses(null, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldListCoursesWithPriceFilter() {
    ListCoursesResponse expectedResponse =
        ListCoursesResponse.builder().courses(List.of()).totalCount(0).build();

    when(mediator.send(any())).thenReturn(expectedResponse);

    ResponseEntity<ListCoursesResponse> response =
        controller.listCourses(new BigDecimal("10"), new BigDecimal("100"));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldEnrollInCourse() {
    StudentPrincipal principal = new StudentPrincipal("test@example.com", 1L);
    EnrollCourseRequest request = EnrollCourseRequest.builder().courseId(10L).build();

    EnrollCourseResponse expectedResponse =
        EnrollCourseResponse.builder()
            .enrollmentId(100L)
            .studentId(1L)
            .courseId(10L)
            .enrollmentDate(LocalDateTime.now())
            .message("Successfully enrolled in course")
            .build();

    when(mediator.send(request)).thenReturn(expectedResponse);

    ResponseEntity<EnrollCourseResponse> response = controller.enroll(principal, request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    EnrollCourseResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(100L, body.getEnrollmentId());
    assertEquals(1L, body.getStudentId());
    assertEquals(10L, body.getCourseId());
    assertEquals(1L, request.getStudentId().longValue());
  }
}
