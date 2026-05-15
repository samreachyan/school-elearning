package com.sakcode.elearning.school.shared.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private int status;
  private String error;
  private String message;
  private String errorCode;
  private LocalDateTime timestamp;
  private String path;
  private List<ValidationError> validationErrors;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ValidationError {
    private String field;
    private String message;
  }
}
