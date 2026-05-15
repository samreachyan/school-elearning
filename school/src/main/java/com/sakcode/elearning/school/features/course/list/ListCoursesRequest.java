package com.sakcode.elearning.school.features.course.list;

import com.sakcode.elearning.school.shared.mediator.IRequest;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListCoursesRequest implements IRequest<ListCoursesResponse> {

  private BigDecimal minPrice;
  private BigDecimal maxPrice;
}
