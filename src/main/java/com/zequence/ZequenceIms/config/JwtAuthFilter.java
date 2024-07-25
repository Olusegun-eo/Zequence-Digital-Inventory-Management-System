package com.zequence.ZequenceIms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zequence.ZequenceIms.dto.ApiErrorResponse;
import com.zequence.ZequenceIms.exceptions.AccessDeniedException;
import com.zequence.ZequenceIms.helper.JwtHelper;
import com.zequence.ZequenceIms.service.logoutService.BlackListedTokenRepository;
import com.zequence.ZequenceIms.service.userService.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;
  private final UserService userService;
  private final BlackListedTokenRepository blacklistedTokenRepository;
  private final JwtHelper jwtHelper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String authHeader = request.getHeader("Authorization");
      String token = null;
      String username = null;

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = jwtHelper.extractUsername(token);
        System.out.println("Extracted token: " + token);
        System.out.println("Extracted username from token: " + username);
      }

      if (token == null || userService.isTokenBlacklisted(token, blacklistedTokenRepository)) {
        System.out.println("Token is null or blacklisted, passing to next filter");
        filterChain.doFilter(request, response);
        return;
      }

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (jwtHelper.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          System.out.println("Authenticated user: " + username);
        }
      }

      filterChain.doFilter(request, response);
    } catch (AccessDeniedException e) {
      System.out.println("JWT Authentication failed: " + e.getMessage());
      ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().write(toJson(errorResponse));
    }
  }

  private String toJson(ApiErrorResponse response) {
    try {
      return objectMapper.writeValueAsString(response);
    } catch (Exception e) {
      return ""; // Return an empty string if serialization fails
    }
  }
}

/*

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;
  private final UserService userService;
  private final BlackListedTokenRepository blacklistedTokenRepository;
  private final JwtHelper jwtHelper;


  @Override //I added @NotNull
  protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
    try {
      //NOTE IF THE LINE OF CODE FROM
        String authHeader = request.getHeader("Authorization");

      String token = null;
      String username = null;
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = jwtHelper.extractUsername(token);
        System.out.println("Extracted token: " + token);
        System.out.println("Extracted username from token: " + username);
      }

      // Early return if token is null or blacklisted
      if (token == null || userService.isTokenBlacklisted(token, blacklistedTokenRepository))  {
        System.out.println("Token is null or blacklisted, passing to next filter");
        filterChain.doFilter(request, response);
        return;
      }

//      String token = jwtHelper.extractTokenFromHeader(request);
//
//            // Early return if token is null or blacklisted
//            if (token == null || jwtHelper.isTokenBlacklisted(token)) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//
//            String username = jwtHelper.extractUsername(token);
//            System.out.println("Extracted token: " + token);
//            System.out.println("Extracted username from token: " + username);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (jwtHelper.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          System.out.println("Authenticated user: " + username);
        }
      }

      filterChain.doFilter(request, response);
    } catch (AccessDeniedException e) {
      System.out.println("JWT Authentication failed: " + e.getMessage());
      ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().write(toJson(errorResponse));
    }
  }

  private String toJson(ApiErrorResponse response) {
    try {
      return objectMapper.writeValueAsString(response);
    } catch (Exception e) {
      return ""; // Return an empty string if serialization fails
    }
  }
}
*/