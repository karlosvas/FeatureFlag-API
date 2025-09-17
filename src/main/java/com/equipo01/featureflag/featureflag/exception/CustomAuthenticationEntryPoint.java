package com.equipo01.featureflag.featureflag.exception;

import com.equipo01.featureflag.featureflag.dto.ErrorDto;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Custom entry point for handling authentication exceptions in Spring Security.
 *
 * <p>This entry point is triggered when an unauthenticated user attempts to access a protected
 * resource. It returns a JSON response with a standardized error structure, including a message,
 * description, HTTP status code, and timestamp.
 *
 * <p>The response has HTTP status 401 (Unauthorized) and content type {@code application/json}.
 * Example response:
 *
 * <pre>
 * {
 *   "message": "Unauthorized",
 *   "description": "JWT token is missing or invalid",
 *   "code": 401,
 *   "timestamp": "2025-09-09T12:34:56.789"
 * }
 * </pre>
 *
 * This class implements {@link org.springframework.security.web.AuthenticationEntryPoint}.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    ErrorDto re =
        ErrorDto.builder()
            .message(MessageError.UNAUTHORIZED.getMessage())
            .description(MessageError.UNAUTHORIZED.getDescription())
            .code(MessageError.UNAUTHORIZED.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    OutputStream responseStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.writeValue(responseStream, re);
    responseStream.flush();
  }
}
