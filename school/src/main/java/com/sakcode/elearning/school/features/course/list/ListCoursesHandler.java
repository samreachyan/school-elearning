package com.sakcode.elearning.school.features.course.list;

import com.sakcode.elearning.school.features.course.Course;
import com.sakcode.elearning.school.features.course.CourseRepository;
import com.sakcode.elearning.school.shared.mediator.IRequestHandler;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCoursesHandler
    implements IRequestHandler<ListCoursesRequest, ListCoursesResponse> {

  private final CourseRepository courseRepository;

  @Override
  public ListCoursesResponse handle(ListCoursesRequest request) {
    List<Course> courses =
        courseRepository.findByPriceRange(request.getMinPrice(), request.getMaxPrice());

    List<CourseDto> courseDtos =
        courses.stream()
            .map(
                course ->
                    CourseDto.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .price(course.getPrice())
                        .instructor(course.getInstructor())
                        .createdAt(course.getCreatedAt())
                        .build())
            .collect(Collectors.toList());

    return ListCoursesResponse.builder().courses(courseDtos).totalCount(courseDtos.size()).build();
  }
}
