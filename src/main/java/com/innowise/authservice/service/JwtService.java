  package com.innowise.authservice.service;
  import com.innowise.authservice.entity.User;
  import com.innowise.authservice.exception.ExpiredTokenException;
  import com.innowise.authservice.exception.InvalidTokenException;
  import io.jsonwebtoken.Claims;
  import io.jsonwebtoken.ExpiredJwtException;
  import io.jsonwebtoken.JwtException;
  import io.jsonwebtoken.Jwts;
  import io.jsonwebtoken.SignatureAlgorithm;
  import io.jsonwebtoken.security.Keys;
  import java.security.Key;
  import java.time.Duration;
  import java.time.Instant;
  import java.util.Base64;
  import java.util.Date;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.stereotype.Service;


  @Service
  public class JwtService{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private Duration accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Duration refreshExpiration;

    public String generateAccessToken(User user) {
      return buildToken(user, accessExpiration, "ACCESS");
    }

    public String generateRefreshToken(User user) {
      return buildToken(user, refreshExpiration, "REFRESH");
    }

    private String buildToken(User user, Duration expiration, String type) {
      return Jwts.builder()
          .setSubject(user.getUsername())
          .claim("role", user.getRole().name())
          .claim("type", type)
          .setIssuedAt(Date.from(Instant.now()))
          .setExpiration(Date.from(Instant.now().plus(expiration)))
          .signWith(getSigningKey(), SignatureAlgorithm.HS256)
          .compact();
    }

    public Claims extractAllClaims(String token) {
      try {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
      } catch (ExpiredJwtException e) {
        throw new ExpiredTokenException();
      } catch (JwtException e) {
        throw new InvalidTokenException();
      }
    }

    private Key getSigningKey() {
      return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }
  }

