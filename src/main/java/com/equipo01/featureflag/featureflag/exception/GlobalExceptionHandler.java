package com.equipo01.featureflag.featureflag.exception;

import com.equipo01.featureflag.featureflag.dto.ErrorDto;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import io.swagger.v3.oas.annotations.Hidden;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * GlobalExceptionHandler centrally manages exceptions thrown by REST controllers.
 *
 * <p>Use {@link RestControllerAdvice} to intercept and customize error responses across the API.
 * Each method handles a specific type of exception and constructs a structured response using
 * {@link ErrorDto}. {@link Hidden} prevents this class from appearing in the generated API
 * documentation.
 *
 * <p>Validations: Returns a list of detailed errors if the input data does not meet the
 * constraints. Resources: Reports whether a feature already exists or is not found. Generic errors:
 * Catches any unhandled exceptions and returns a 500 error.
 *
 * <p>This improves the customer experience and facilitates debugging in development and production.
 */
@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

  /**
   * Handles any exception not specifically managed by other methods.
   *
   * @param ex Generic exception thrown anywhere in the application.
   * @return Error reporting that an unexpected problem occurred. Returns an {@link ErrorResponse}
   *     with code 500 (INTERNAL SERVER ERROR).
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
    if (ex instanceof AccessDeniedException || ex instanceof AuthenticationException) {
      throw (RuntimeException) ex; // deja que lo maneje Spring Security
    }
    ErrorDto errorResponse =
        ErrorDto.builder()
            .message(MessageError.INTERNAL_SERVER_ERROR.getMessage())
            .description(ex.getMessage())
            .code(MessageError.INTERNAL_SERVER_ERROR.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(MessageError.INTERNAL_SERVER_ERROR.getStatus())
        .body(errorResponse);
  }

  /**
   * Handles exceptions related to database access.
   *
   * @param ex Exception thrown when interacting with the database.
   * @return Error informing that a problem occurred in the database. Returns a {@link
   *     ErrorResponse} with code 500 (INTERNAL SERVER ERROR).
   */
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorDto> handleDataAccessException(DataAccessException ex) {
    ErrorDto errorResponse =
        ErrorDto.builder()
            .message(MessageError.DATA_ACCESS_ERROR.getMessage())
            .description(ex.getMessage())
            .code(MessageError.DATA_ACCESS_ERROR.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(MessageError.DATA_ACCESS_ERROR.getStatus()).body(errorResponse);
  }

  /**
   * Handles validation exceptions for arguments in REST endpoints.
   *
   * <p>When a DTO does not meet validation constraints (e.g., @NotNull, @Size), this method
   * collects all errors.
   *
   * @param ex Exception thrown by Spring when validation fails.
   * @return List of errors with specific details and messages, returned in a list of {@link
   *     ErrorResponse} with code 400 (BAD REQUEST).
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorDto>> handleValidationException(
      MethodArgumentNotValidException ex) {
    List<ErrorDto> errorResponses = new ArrayList<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              ErrorDto errorResponse =
                  ErrorDto.builder()
                      .message(MessageError.DTO_FIELDS_NOT_VALID.getMessage())
                      .description(error.getDefaultMessage())
                      .code(MessageError.DTO_FIELDS_NOT_VALID.getStatus().value())
                      .timestamp(LocalDateTime.now())
                      .build();
              errorResponses.add(errorResponse);
            });
    return ResponseEntity.status(MessageError.DTO_FIELDS_NOT_VALID.getStatus())
        .body(errorResponses);
  }

  /**
   * Handles the exception when a malformed JSON is received in the request.
   *
   * @param ex Exception thrown by Spring when the JSON cannot be read.
   * @return Error informing that the request body is not valid JSON. Returns an {@link
   *     ErrorResponse} with code 400 (BAD REQUEST).
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleJsonParseError(HttpMessageNotReadableException ex) {
    ErrorDto errorResponse =
        ErrorDto.builder()
            .message(MessageError.MALFORMED_JSON.getMessage())
            .description(ex.getMessage())
            .code(MessageError.MALFORMED_JSON.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(MessageError.MALFORMED_JSON.getStatus()).body(errorResponse);
  }

  /**
   * Handles the exception when a method parameter validation fails.
   *
   * @param ex Exception thrown by the Java validation system.
   * @return Error reporting that a validation constraint was violated. Returns an {@link
   *     ErrorResponse} with code 400 (BAD REQUEST).
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<List<ErrorDto>> handleMethodValidation(
      HandlerMethodValidationException ex) {
    List<ErrorDto> errorResponses = new ArrayList<>();
    ex.getAllErrors()
        .forEach(
            (error) -> {
              ErrorDto errorResponse =
                  ErrorDto.builder()
                      .message(MessageError.VALIDATION_PARAMETER_NOT_VALID.getMessage())
                      .description(error.getDefaultMessage())
                      .code(MessageError.VALIDATION_PARAMETER_NOT_VALID.getStatus().value())
                      .timestamp(LocalDateTime.now())
                      .build();
              errorResponses.add(errorResponse);
            });
    return ResponseEntity.status(MessageError.VALIDATION_PARAMETER_NOT_VALID.getStatus())
        .body(errorResponses);
  }

  /**
   * Handles the exception when an unsupported HTTP method is used on the endpoint.
   *
   * @param ex Exception thrown by Spring when the HTTP method is not supported.
   * @return Error informing that the HTTP method is not allowed. Returns an {@link ErrorResponse}
   *     with code 405 (METHOD NOT ALLOWED).
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex) {
    ErrorDto errorResponse =
        ErrorDto.builder()
            .message(MessageError.METHOD_NOT_ALLOWED.getMessage())
            .description(ex.getMessage())
            .code(MessageError.METHOD_NOT_ALLOWED.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(MessageError.METHOD_NOT_ALLOWED.getStatus()).body(errorResponse);
  }

  /**
   * Handles the custom exception.
   *
   * @param ex Custom exception thrown by the business logic.
   * @return Error reporting business logic validations. Returns a {@link ErrorDto}.
   */
  @ExceptionHandler(FeatureFlagException.class)
  public ResponseEntity<ErrorDto> handleFeatureFlagException(FeatureFlagException ex) {
    if (HttpStatus.NO_CONTENT.equals(ex.getStatus())) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    ErrorDto errorResponse =
        ErrorDto.builder()
            .message(ex.getMessage())
            .description(ex.getDescription())
            .code(ex.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(ex.getStatus()).body(errorResponse);
  }
}
