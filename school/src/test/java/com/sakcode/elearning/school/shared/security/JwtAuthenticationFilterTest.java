package com.sakcode.elearning.school.shared.security;

import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Mock private JwtTokenProvider jwtTokenProvider;
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;

  private JwtAuthenticationFilter filter;

  @BeforeEach
  void setUp() {
    filter = new JwtAuthenticationFilter(jwtTokenProvider);
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldSetAuthenticationWhenValidTokenProvided() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
    when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);
    when(jwtTokenProvider.getEmailFromToken("valid-token")).thenReturn("test@example.com");
    when(jwtTokenProvider.getStudentIdFromToken("valid-token")).thenReturn(1L);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assert SecurityContextHolder.getContext().getAuthentication() != null;
    assert SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        instanceof StudentPrincipal;
    StudentPrincipal principal =
        (StudentPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assert "test@example.com".equals(principal.getEmail());
    assert 1L == principal.getStudentId();
  }

  @Test
  void shouldNotSetAuthenticationWhenNoTokenProvided() throws Exception {
    when(request.getHeader("Authorization")).thenReturn(null);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assert SecurityContextHolder.getContext().getAuthentication() == null;
  }

  @Test
  void shouldNotSetAuthenticationWhenInvalidTokenProvided() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
    when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assert SecurityContextHolder.getContext().getAuthentication() == null;
  }

  @Test
  void shouldNotSetAuthenticationWhenTokenWithoutBearerPrefix() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("NotBearer token");

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assert SecurityContextHolder.getContext().getAuthentication() == null;
  }

  @Test
  void shouldNotSetAuthenticationWhenEmptyAuthorizationHeader() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("");

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assert SecurityContextHolder.getContext().getAuthentication() == null;
  }
}
