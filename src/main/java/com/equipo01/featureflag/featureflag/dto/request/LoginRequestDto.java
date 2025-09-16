package com.equipo01.featureflag.featureflag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user authentication requests.
 * 
 * This DTO represents the credentials required for user authentication in the
 * feature flag management system. It encapsulates the username and password
 * combination that users provide to gain access to the application and obtain
 * authentication tokens for subsequent API operations.
 * 
 * The DTO is specifically designed for stateless authentication flows where
 * users provide credentials to receive JWT tokens or similar authentication
 * artifacts. It includes comprehensive validation to ensure credential
 * integrity and prevent authentication attempts with malformed data.
 * 
 * Security considerations:
 * - Password validation ensures minimum security requirements
 * - Username validation prevents injection attacks
 * - Field size limits prevent buffer overflow scenarios
 * - No password storage or exposure in logs or responses
 * 
 * Authentication flow:
 * 1. Client submits credentials using this DTO
 * 2. Server validates credentials against user database
 * 3. Server returns authentication token upon successful validation
 * 4. Client uses token for subsequent authenticated API calls
 * 
 * Key characteristics:
 * - Strict validation for security compliance
 * - Size constraints for system stability
 * - Swagger/OpenAPI integration for documentation
 * - Builder pattern for convenient object construction
 * 
 * JSON structure example:
 * <pre>
 * {
 *   "username": "usuario01",
 *   "password": "securePassword123"
 * }
 * </pre>
 * 
 */
@Getter
@Setter
@Builder
@Schema(description = "DTO for user login request")
public class LoginRequestDto {
  
  /**
   * Username for authentication.
   * 
   * Username requirements:
   * - Must be between 3 and 50 characters in length
   * - Cannot be blank or contain only whitespace
   * - Should match the username used during account creation
   */
  @Schema(description = "Username for login", example = "usuario01")
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  /**
   * Password for authentication.
   * 
   * Password requirements:
   * - Must be between 6 and 100 characters in length
   * - Cannot be blank or contain only whitespace
   * - Should meet the organization's password complexity requirements
   */
  @Schema(description = "User password", example = "securePassword123")
  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
  private String password;
}
