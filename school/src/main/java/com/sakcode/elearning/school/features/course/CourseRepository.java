package com.sakcode.elearning.school.features.course;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query(
      "SELECT c FROM Course c WHERE (:minPrice IS NULL OR c.price >= :minPrice) AND (:maxPrice IS NULL OR c.price <= :maxPrice)")
  List<Course> findByPriceRange(
      @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
}
