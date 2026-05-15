package com.sakcode.elearning.school.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final String secret;
  private final long expirationMs;

  public JwtTokenProvider(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration-ms}") long expirationMs) {
    this.secret = secret;
    this.expirationMs = expirationMs;
  }

  public String generateToken(String email, Long studentId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(email)
        .claim("studentId", studentId)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public String getEmailFromToken(String token) {
    Claims claims = parseToken(token);
    return claims.getSubject();
  }

  public Long getStudentIdFromToken(String token) {
    Claims claims = parseToken(token);
    return claims.get("studentId", Long.class);
  }

  public boolean validateToken(String token) {
    try {
      parseToken(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Claims parseToken(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes =
        Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secret.getBytes()));
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
