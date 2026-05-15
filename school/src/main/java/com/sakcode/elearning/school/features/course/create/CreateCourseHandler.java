package com.sakcode.elearning.school.features.course.create;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCourseHandler
    implements IRequestHandler<CreateCourseRequest, CreateCourseResponse> {

  private final CourseRepository courseRepository;

  @Override
  public CreateCourseResponse handle(CreateCourseRequest request) {
    Course course =
        new Course(
            request.getTitle(),
            request.getDescription(),
            request.getPrice(),
            request.getInstructor());

    course = courseRepository.save(course);

    return CreateCourseResponse.builder()
        .id(course.getId())
        .title(course.getTitle())
        .description(course.getDescription())
        .price(course.getPrice())
        .instructor(course.getInstructor())
        .createdAt(course.getCreatedAt())
        .message("Course created successfully")
        .build();
  }
}
