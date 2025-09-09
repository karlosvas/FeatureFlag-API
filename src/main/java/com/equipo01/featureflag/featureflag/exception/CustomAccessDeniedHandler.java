package com.equipo01.featureflag.featureflag.exception;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.equipo01.featureflag.featureflag.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom handler for access denied exceptions in Spring Security.
 * <p>
 * This handler is triggered when a user tries to access a protected resource
 * without the necessary permissions. It returns a JSON response with a
 * standardized error structure, including a message, description, HTTP status
 * code,
 * and timestamp.
 * </p>
 *
 * <p>
 * The response has HTTP status 403 (Forbidden) and content type
 * {@code application/json}.
 * </p>
 *
 * Example response:
 * 
 * <pre>
 * {
 *   "message": "Forbidden",
 *   "description": "You don't have permission to access this resource",
 *   "code": 403,
 *   "timestamp": "2025-09-09T12:34:56.789"
 * }
 * </pre>
 *
 * This class implements
 * {@link org.springframework.security.web.access.AccessDeniedHandler}.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Forbidden")
                .description("You don't have permission to access this resource")
                .code(HttpStatus.FORBIDDEN.value())
                .timestamp(LocalDateTime.now())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }
}
