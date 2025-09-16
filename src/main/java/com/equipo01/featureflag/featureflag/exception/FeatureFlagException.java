package com.equipo01.featureflag.featureflag.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom runtime exception for feature flag related business logic violations and errors.
 * 
 * This exception serves as the primary exception type for all feature flag operations
 * within the application. It provides comprehensive error information including HTTP
 * status codes, detailed messages, and descriptive explanations to support proper
 * error handling and client communication.
 * 
 * The exception is designed to work seamlessly with REST API error handling mechanisms,
 * providing structured error information that can be easily transformed into appropriate
 * HTTP responses. It extends RuntimeException to provide unchecked exception behavior,
 * allowing it to be thrown without explicit declaration while maintaining meaningful
 * error context.
 * 
 * Key features:
 * - HTTP status code integration for REST API compatibility
 * - Structured error messaging with multiple detail levels
 * - Support for various feature flag operation failures
 * - Lombok integration for automatic getter generation
 * 
 * Common usage scenarios:
 * - Feature not found errors (404 Not Found)
 * - Feature configuration conflicts (409 Conflict)
 * - Invalid feature operations (400 Bad Request)
 * - Internal feature processing errors (500 Internal Server Error)
 * 
 */
@Getter
public class FeatureFlagException extends RuntimeException {

  /** 
   * HTTP status code that should be returned in the API response.
   * This field enables proper REST API error handling by providing
   * the appropriate HTTP status code for the specific error condition.
   */
  private final HttpStatus status;
  
  /** 
   * Brief error message describing the nature of the exception.
   * This message is typically used for logging and as the primary
   * error description in API responses.
   */
  private final String message;
  
  /** 
   * Detailed description providing additional context about the error.
   */
  private final String description;

  /**
   * Constructs a new FeatureFlagException with comprehensive error information.
   * 
   * @param status The HTTP status code that should be returned in API responses.
   *               Should reflect the nature of the error (e.g., NOT_FOUND, CONFLICT, BAD_REQUEST).
   * @param message A concise error message describing what went wrong.
   *                This message is used as the RuntimeException message and in API responses.
   * @param description A detailed explanation providing additional context about the error,
   *                    potentially including suggestions for resolution or debugging information.
   * @throws IllegalArgumentException if any of the parameters are null
   */
  public FeatureFlagException(HttpStatus status, String message, String description) {
    super(message);
    this.status = status;
    this.message = message;
    this.description = description;
  }
}
