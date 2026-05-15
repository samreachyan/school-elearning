package com.sakcode.elearning.school.features.progress;

import com.sakcode.elearning.school.features.progress.generatecertificate.GenerateCertificateRequest;
import com.sakcode.elearning.school.features.progress.generatecertificate.GenerateCertificateResponse;
import com.sakcode.elearning.school.features.progress.getprogress.GetCourseProgressRequest;
import com.sakcode.elearning.school.features.progress.getprogress.GetCourseProgressResponse;
import com.sakcode.elearning.school.features.progress.marklesson.MarkLessonCompletedRequest;
import com.sakcode.elearning.school.features.progress.marklesson.MarkLessonCompletedResponse;
import com.sakcode.elearning.school.shared.mediator.Mediator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class ProgressController {

  private final Mediator mediator;

  @PostMapping("/lessons/complete")
  public ResponseEntity<MarkLessonCompletedResponse> markLessonCompleted(
      @Valid @RequestBody MarkLessonCompletedRequest request) {
    MarkLessonCompletedResponse response = mediator.send(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<GetCourseProgressResponse> getProgress(@RequestParam Long enrollmentId) {
    GetCourseProgressRequest request =
        GetCourseProgressRequest.builder().enrollmentId(enrollmentId).build();
    GetCourseProgressResponse response = mediator.send(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/certificate")
  public ResponseEntity<GenerateCertificateResponse> generateCertificate(
      @Valid @RequestBody GenerateCertificateRequest request) {
    GenerateCertificateResponse response = mediator.send(request);
    return ResponseEntity.ok(response);
  }
}
