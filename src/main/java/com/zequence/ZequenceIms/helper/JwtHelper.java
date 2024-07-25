package com.zequence.ZequenceIms.helper;

import com.zequence.ZequenceIms.exceptions.AccessDeniedException;
import com.zequence.ZequenceIms.service.logoutService.BlackListedToken;
import com.zequence.ZequenceIms.service.logoutService.BlackListedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtHelper {

  private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final int EXPIRATION_TIME_IN_MINUTES = 60;

  @Autowired
  private BlackListedTokenRepository blacklistedTokenRepository;

  public String generateToken(String email) {
    long nowMillis = System.currentTimeMillis();
    long expMillis = nowMillis + EXPIRATION_TIME_IN_MINUTES * 60 * 1000;
    Date exp = new Date(expMillis);

    return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date(nowMillis))
            .setExpiration(exp)
            .signWith(SECRET_KEY)
            .compact();
  }

  public String extractTokenFromHeader(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  public  String extractUsername(String token) {
    return getTokenBody(token).getSubject();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    if (isTokenBlacklisted(token)) {
      return false; // Token is blacklisted
    }
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
  public boolean isTokenBlacklisted(String token) {
    return blacklistedTokenRepository.existsByToken(token);
  }

  private Claims getTokenBody(String token) {
    try {
      return Jwts.parserBuilder()
              .setSigningKey(SECRET_KEY)
              .build()
              .parseClaimsJws(token)
              .getBody();
    } catch (SignatureException | ExpiredJwtException e) {
      throw new AccessDeniedException("Access denied: " + e.getMessage());
    }
  }

  private boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }

  public Date getExpiryDateFromToken(String token) {
    return getTokenBody(token).getExpiration();
  }


  public void blacklistToken(String token) {
    Date expiryDate = getExpiryDateFromToken(token);

    BlackListedToken blacklistedToken = new BlackListedToken();
    blacklistedToken.setToken(token);
    blacklistedToken.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

    blacklistedTokenRepository.save(blacklistedToken);
  }
}
