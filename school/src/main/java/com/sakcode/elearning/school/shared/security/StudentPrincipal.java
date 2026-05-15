package com.sakcode.elearning.school.shared.security;

import java.security.Principal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentPrincipal implements Principal {

  private final String email;
  private final Long studentId;

  @Override
  public String getName() {
    return email;
  }
}
