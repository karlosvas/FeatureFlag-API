package com.equipo01.featureflag.featureflag.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT-based authorization filter. -Extends {@link OncePerRequestFilter} to ensure it runs once per
 * request. -Interprets and validates the JWT token in the Authorization header of requests. -If the
 * token is valid, extracts the username and sets the authentication in the security context. -If
 * the token is invalid or not present, the request continues without authorization. -This filter
 * applies to all requests protected by Spring Security except those specified.
 *
 * @author alex
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  // Dependency for working with JWT tokens (generate, validate, extract data)
  private final JwtUtil jwtUtil;

  // Dependency for working with user details
  private final CustomUserDetailsService uds;

  /**
   * Constructor to initialize filter dependencies
   *
   * @param jwtUtil
   */
  public JwtAuthorizationFilter(JwtUtil jwtUtil, CustomUserDetailsService uds) {
    this.jwtUtil = jwtUtil;
    this.uds = uds;
  }

  /**
   * MMethod that is executed for each HTTP request. -1. Obtains the JWT token from the
   * Authorization header. -2. Validates the token using {@link JwtUtil}. -3. If the token is valid,
   * extracts the username and creates an authentication object. -4. Establishes the authentication
   * in the security context. -5. Continues with the filter chain.
   *
   * @param request HTTP request
   * @param response HTTP response
   * @param filterChain filter chain
   * @throws ServletException if a servlet error occurs
   * @throws IOException if an input/output error occurs
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // 1. Obtains the token from the request
    String token = getJwtFromRequest(request);
    // 2. Validates the token
    if (token != null && jwtUtil.validateToken(token)) {
      try {

        String username = jwtUtil.getUsernameFromJWT(token);
        UserDetails userDetails = uds.loadUserByUsername(username);
        
        UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception ex) {
        logger.error("Could not set user authentication in security context", ex);
      }
    }
    // 3. Continues with the filter chain
    filterChain.doFilter(request, response);
  }

  /**
   * Method that obtains the JWT token from the Authorization header of the HTTP request. -1.
   * Obtains the content of the Authorization header. -2. Verifies that the header is not null and
   * starts with Bearer . -3. Extracts the token by removing the Bearer prefix. -4. If the header is
   * not valid, returns null.
   *
   * @param request HTTP request
   * @return the JWT token without the Bearer prefix, or null if not present
   */
  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
