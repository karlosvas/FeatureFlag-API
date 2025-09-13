package com.equipo01.featureflag.featureflag.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.equipo01.featureflag.featureflag.dto.ErrorResponse;

/**
 * GlobalExceptionHandler centrally manages exceptions
 * thrown by REST controllers.
 *
 * Use {@link RestControllerAdvice} to intercept and customize
 * error responses across the API.
 * Each method handles a specific type of exception and constructs a structured response
 * using {@link ErrorResponse}.
 *
 * Validations: Returns a list of detailed errors if the input data does not meet the constraints.
 * Resources: Reports whether a feature already exists or is not found.
 * Generic errors: Catches any unhandled exceptions and returns a 500 error.
 *
 * This improves the customer experience and facilitates debugging in development and production.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles any exception not specifically managed by other methods.
     *
     * @param ex Generic exception thrown anywhere in the application.
     * @return Error reporting that an unexpected problem occurred. Returns an
     *         {@link ErrorResponse} with code 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Internal Server Error")
                .description(ex.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handles exceptions related to database access.
     *
     * @param ex Exception thrown when interacting with the database.
     * @return Error informing that a problem occurred in the database. Returns
     *         a {@link ErrorResponse} with code 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Error accessing data")
                .description(ex.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handles validation exceptions for arguments in REST endpoints.
     *
     * When a DTO does not meet validation constraints (e.g.,
     * @NotNull, @Size),
     * this method collects all errors.
     *
     * @param ex Exception thrown by Spring when validation fails.
     * @return List of errors with specific details and messages, returned in
     *         a list of {@link ErrorResponse} with code 400 (BAD REQUEST).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message("Validation Error")
                    .description(error.getDefaultMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .timestamp(LocalDateTime.now())
                    .build();
            errorResponses.add(errorResponse);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }

    /**
     * Handles the exception when a malformed JSON is received in the request.
     *
     * @param ex Exception thrown by Spring when the JSON cannot be read.
     * @return Error informing that the request body is not valid JSON.
     *         Returns an {@link ErrorResponse} with code 400 (BAD REQUEST).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Malformed JSON Request")
                .description(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles the exception when a method parameter validation fails.
     *
     * @param ex Exception thrown by the Java validation system.
     * @return Error reporting that a validation constraint was violated.
     *         Returns an {@link ErrorResponse} with code 400 (BAD REQUEST).
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodValidation(HandlerMethodValidationException ex) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        ex.getAllErrors().forEach((error) -> {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message("Constraint Violation")
                    .description(error.getDefaultMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .timestamp(LocalDateTime.now())
                    .build();
            errorResponses.add(errorResponse);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }

    /**
     * Handles the exception when an unsupported HTTP method is used on the endpoint.
     *
     * @param ex Exception thrown by Spring when the HTTP method is not supported.
     * @return Error informing that the HTTP method is not allowed. Returns an
     *         {@link ErrorResponse} with code 405 (METHOD NOT ALLOWED).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Method Not Allowed")
                .description(ex.getMessage())
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * Handles the exception when authentication credentials are invalid.
     *
     * @param ex exception thrown.
     * @return Error informing that the credentials are incorrect. Returns a
     *         {@link ErrorResponse} with code 400 (BAD_REQUEST).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Authentication Failed")
                .description(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles the exception when attempting to create a feature that already exists.
     *
     * @param ex Custom exception thrown by the business logic.
     * @return Error informing that the feature already exists. Returns a
     *         {@link ErrorResponse} with code 409 (CONFLICT) and details of the error.
     */
    @ExceptionHandler(FeatureAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFeatureAlreadyExists(FeatureAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Feature Already Exists")
                .description(ex.getMessage())
                .code(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handles the exception when a requested feature is not found.
     *
     * @param ex Custom exception thrown by the business logic.
     * @return Error informing that the feature does not exist. Returns a
     *         {@link ErrorResponse} with code 404 (NOT FOUND) and details of the
     *         error.
     */
    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeatureNotFound(FeatureNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Feature Not Found")
                .description(ex.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles the exception when a user already exists.
     *
     * @param ex Custom exception thrown by the business logic.
     * @return Error informing that the user already exists. Returns a
     *         {@link ErrorResponse} with code 409 (CONFLICT).
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("User Already Exists")
                .description(ex.getMessage())
                .code(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}