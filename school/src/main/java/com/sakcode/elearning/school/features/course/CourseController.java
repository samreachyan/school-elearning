package com.sakcode.elearning.school.features.course;

import com.sakcode.elearning.school.features.course.create.CreateCourseRequest;
import com.sakcode.elearning.school.features.course.create.CreateCourseResponse;
import com.sakcode.elearning.school.features.course.enroll.EnrollCourseRequest;
import com.sakcode.elearning.school.features.course.enroll.EnrollCourseResponse;
import com.sakcode.elearning.school.features.course.list.ListCoursesRequest;
import com.sakcode.elearning.school.features.course.list.ListCoursesResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import com.sakcode.elearning.school.shared.security.StudentPrincipal;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

  private final Mediator mediator;

  @PostMapping
  public ResponseEntity<CreateCourseResponse> createCourse(
      @Valid @RequestBody CreateCourseRequest request) {
    CreateCourseResponse response = mediator.send(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<ListCoursesResponse> listCourses(
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice) {
    ListCoursesRequest request =
        ListCoursesRequest.builder().minPrice(minPrice).maxPrice(maxPrice).build();
    ListCoursesResponse response = mediator.send(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/enroll")
  public ResponseEntity<EnrollCourseResponse> enroll(
      @AuthenticationPrincipal StudentPrincipal principal,
      @Valid @RequestBody EnrollCourseRequest request) {
    request.setStudentId(principal.getStudentId());
    EnrollCourseResponse response = mediator.send(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
