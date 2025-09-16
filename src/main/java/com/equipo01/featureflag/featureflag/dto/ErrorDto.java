package com.equipo01.featureflag.featureflag.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for transporting error information in API responses.
 * 
 * This DTO is designed to provide structured error information to API clients,
 * encapsulating exception details in a standardized format that maintains
 * consistency across the application. It serves as the primary mechanism for
 * error communication between different layers of the application without
 * exposing internal implementation details or sensitive system information.
 * 
 * The ErrorDto follows RFC 7807 (Problem Details for HTTP APIs) principles
 * by providing structured error information that includes both human-readable
 * descriptions and machine-readable status codes. This enables clients to
 * handle errors appropriately and provide meaningful feedback to end users.
 * 
 * Key features:
 * - Structured error information with multiple detail levels
 * - HTTP status code integration for REST API compatibility
 * - Timestamp tracking for error occurrence and debugging
 * - Swagger/OpenAPI documentation support for API documentation
 * - Builder pattern support for flexible object construction
 * 
 * Common usage scenarios:
 * - Validation errors with detailed field-specific messages
 * - Business logic violations with contextual information
 * - Authentication and authorization failures
 * - Resource not found errors with helpful suggestions
 * - Internal server errors with correlation IDs for debugging
 * 
 * JSON structure example:
 * <pre>
 * {
 *   "message": "Validation Failed",
 *   "description": "The request contains invalid data in multiple fields",
 *   "code": 400,
 *   "timestamp": "2025-09-16 14:30:45"
 * }
 * </pre>
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for transporting error information in API responses.")
public class ErrorDto {
  
  /**
   * Brief error message providing a summary of what went wrong.
   * 
   * The message should follow consistent naming conventions and avoid
   * exposing sensitive system information or internal implementation details.
   */
  @Schema(description = "Brief error message summarizing what went wrong", example = "Unauthorized")
  private String message;

  /**
   * Detailed error description providing additional context and guidance.
   * 
   * The description should be helpful without revealing sensitive system
   * details or security-related information that could be exploited.
   */
  @Schema(
      description = "Detailed error description with additional context and guidance",
      example = "You do not have the required permissions to access this resource. Please contact your administrator or ensure you are properly authenticated.")
  private String description;

  /**
   * HTTP status code associated with the error.
   */
  @Schema(description = "HTTP status code associated with the error", example = "401")
  private int code;

  /**
   * Timestamp indicating when the error occurred.
   */
  @Schema(
      description = "Timestamp indicating when the error occurred",
      example = "2025-09-16 14:30:45")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;
}
