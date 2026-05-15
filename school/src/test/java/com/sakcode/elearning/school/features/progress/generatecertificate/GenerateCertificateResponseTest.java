package com.sakcode.elearning.school.features.progress.generatecertificate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class GenerateCertificateResponseTest {

  @Test
  void shouldCreateResponseUsingBuilder() {
    LocalDateTime now = LocalDateTime.now();
    GenerateCertificateResponse response =
        GenerateCertificateResponse.builder()
            .certificateId("CERT-123")
            .studentId(1L)
            .studentName("John Doe")
            .courseId(100L)
            .courseTitle("Java")
            .completedAt(now)
            .message("Certificate generated successfully")
            .build();

    assertEquals("CERT-123", response.getCertificateId());
    assertEquals(1L, response.getStudentId());
    assertEquals("John Doe", response.getStudentName());
    assertEquals(100L, response.getCourseId());
    assertEquals("Java", response.getCourseTitle());
    assertEquals(now, response.getCompletedAt());
    assertEquals("Certificate generated successfully", response.getMessage());
  }

  @Test
  void shouldUseNoArgsConstructor() {
    GenerateCertificateResponse response = new GenerateCertificateResponse();
    assertNull(response.getCertificateId());
    assertNull(response.getStudentId());
    assertNull(response.getStudentName());
    assertNull(response.getCourseId());
    assertNull(response.getCourseTitle());
    assertNull(response.getCompletedAt());
    assertNull(response.getMessage());
  }

  @Test
  void shouldUseAllArgsConstructor() {
    LocalDateTime now = LocalDateTime.now();
    GenerateCertificateResponse response =
        new GenerateCertificateResponse("CERT-456", 1L, "Jane", 100L, "Python", now, "Done");

    assertEquals("CERT-456", response.getCertificateId());
    assertEquals(1L, response.getStudentId());
    assertEquals("Jane", response.getStudentName());
    assertEquals(100L, response.getCourseId());
    assertEquals("Python", response.getCourseTitle());
    assertEquals(now, response.getCompletedAt());
    assertEquals("Done", response.getMessage());
  }

  @Test
  void shouldSetAndGetFields() {
    GenerateCertificateResponse response = new GenerateCertificateResponse();
    response.setCertificateId("CERT-789");
    response.setStudentId(2L);
    response.setStudentName("Alice");
    response.setCourseId(200L);
    response.setCourseTitle("Spring Boot");
    response.setCompletedAt(null);
    response.setMessage("Generated");

    assertEquals("CERT-789", response.getCertificateId());
    assertEquals(2L, response.getStudentId());
    assertEquals("Alice", response.getStudentName());
    assertEquals(200L, response.getCourseId());
    assertEquals("Spring Boot", response.getCourseTitle());
    assertNull(response.getCompletedAt());
    assertEquals("Generated", response.getMessage());
  }
}
