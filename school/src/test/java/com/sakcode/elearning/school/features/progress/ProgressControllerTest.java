package com.sakcode.elearning.school.features.progress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sakcode.elearning.school.features.progress.generatecertificate.GenerateCertificateRequest;
import com.sakcode.elearning.school.features.progress.generatecertificate.GenerateCertificateResponse;
import com.sakcode.elearning.school.features.progress.getprogress.GetCourseProgressResponse;
import com.sakcode.elearning.school.features.progress.marklesson.MarkLessonCompletedRequest;
import com.sakcode.elearning.school.features.progress.marklesson.MarkLessonCompletedResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProgressControllerTest {

  @Mock private Mediator mediator;

  private ProgressController controller;

  @BeforeEach
  void setUp() {
    controller = new ProgressController(mediator);
  }

  @Test
  void shouldMarkLessonCompleted() {
    MarkLessonCompletedRequest request =
        MarkLessonCompletedRequest.builder().enrollmentId(1L).lessonId(10L).build();

    MarkLessonCompletedResponse expectedResponse =
        MarkLessonCompletedResponse.builder()
            .lessonCompletionId(50L)
            .enrollmentId(1L)
            .lessonId(10L)
            .completedAt(LocalDateTime.now())
            .progressPercentage(new BigDecimal("20.00"))
            .message("Lesson marked as completed")
            .build();

    when(mediator.send(request)).thenReturn(expectedResponse);

    ResponseEntity<MarkLessonCompletedResponse> response = controller.markLessonCompleted(request);

    assertEquals(200, response.getStatusCode().value());
    MarkLessonCompletedResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(50L, body.getLessonCompletionId());
    assertEquals("Lesson marked as completed", body.getMessage());
  }

  @Test
  void shouldGetProgress() {
    GetCourseProgressResponse expectedResponse =
        GetCourseProgressResponse.builder()
            .enrollmentId(1L)
            .studentId(1L)
            .courseId(100L)
            .courseTitle("Java")
            .progressPercentage(new BigDecimal("50.00"))
            .totalLessons(4)
            .completedLessons(2)
            .lessons(List.of())
            .build();

    when(mediator.send(any())).thenReturn(expectedResponse);

    ResponseEntity<GetCourseProgressResponse> response = controller.getProgress(1L);

    assertEquals(200, response.getStatusCode().value());
    GetCourseProgressResponse body = response.getBody();
    assertNotNull(body);
    assertEquals(1L, body.getEnrollmentId());
    assertEquals("Java", body.getCourseTitle());
    assertEquals(2, body.getCompletedLessons());
  }

  @Test
  void shouldGenerateCertificate() {
    GenerateCertificateRequest request =
        GenerateCertificateRequest.builder().enrollmentId(1L).build();

    GenerateCertificateResponse expectedResponse =
        GenerateCertificateResponse.builder()
            .certificateId("CERT-123")
            .studentId(1L)
            .studentName("John Doe")
            .courseId(100L)
            .courseTitle("Java")
            .completedAt(LocalDateTime.now())
            .message("Certificate generated successfully")
            .build();

    when(mediator.send(request)).thenReturn(expectedResponse);

    ResponseEntity<GenerateCertificateResponse> response =
        controller.generateCertificate(request);

    assertEquals(200, response.getStatusCode().value());
    GenerateCertificateResponse body = response.getBody();
    assertNotNull(body);
    assertEquals("CERT-123", body.getCertificateId());
    assertEquals("John Doe", body.getStudentName());
  }
}
