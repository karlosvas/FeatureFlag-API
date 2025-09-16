package com.equipo01.featureflag.featureflag.dto.request;

import com.equipo01.featureflag.featureflag.anotations.NotCompromised;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user registration requests.
 * 
 * This DTO represents the data required to create a new user account in the
 * feature flag management system. It encapsulates the essential information
 * needed for user registration including credentials, contact information,
 * and initial account settings.
 * 
 * The DTO is specifically designed for new user registration flows where
 * administrators or users themselves provide the necessary information to
 * establish a new account. It includes comprehensive validation to ensure
 * data integrity and security compliance throughout the registration process.
 * 
 * Security features:
 * - Password compromise validation using security databases
 * - Email format validation for communication reliability
 * - Username validation for system consistency
 * - Input sanitization through size constraints
 * 
 * Registration flow:
 * 1. Client submits registration data using this DTO
 * 2. Server validates all fields including password security
 * 3. Server creates new user account with default permissions
 * 4. Server returns confirmation or authentication token
 * 
 * Default user settings:
 * - Role: USER (standard permissions)
 * - Status: Active (enabled for immediate use)
 * - Permissions: Basic feature flag access
 * 
 * Key characteristics:
 * - Comprehensive validation for security and data integrity
 * - Email validation for communication purposes
 * - Password security validation against known compromises
 * - Swagger/OpenAPI integration for API documentation
 * - Builder pattern support for flexible object construction
 * 
 * JSON structure example:
 * <pre>
 * {
 *   "username": "usuario01",
 *   "email": "usuario01@email.com",
 *   "password": "securePassword123"
 * }
 * </pre>
 */
@Getter
@Setter
@Builder
@Schema(description = "DTO for user creation request")
public class UserRequestDTO {
    
    /**
     * The username for the new user account.
     * 
     * The username must meet specific criteria for security and system consistency:
     * - Must be between 3 and 50 characters in length
     * - Must be unique across the entire system
     * - Should contain only alphanumeric characters and underscores
     * - Will be used for login authentication and user references
     * 
     * The username is case-sensitive and cannot be changed after account creation
     * to maintain system integrity and audit trail consistency.
     */
    @Schema(description = "Username for login", example = "usuario01")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * The email address for the new user account.
     * 
     * The email address can be updated after account creation but requires
     * proper verification to maintain account security.
     */
    @Schema(description = "Email of the user", example = "usuario01@email.com")
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * The password for the new user account.
     * 
     * Security features and validation:
     * - Minimum length: 6 characters (basic security requirement)
     * - Maximum length: 100 characters (prevents denial-of-service attacks)
     * - Compromise validation: Checked against known compromised password databases
     * - Encryption: Automatically hashed and salted before storage
     * - Never stored in plain text within the system
     * 
     * The password can be changed after account creation through secure
     * password reset mechanisms that require proper identity verification.
     */
    @Schema(description = "User password", example = "passwordSeguro123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @NotCompromised
    private String password;
}
