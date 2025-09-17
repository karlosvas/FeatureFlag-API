package com.equipo01.featureflag.featureflag.controller.impl;

import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.service.UserService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import javax.security.auth.login.AccountLockedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller implementation for managing user-related operations.
 *
 * <p>This controller provides comprehensive endpoints for user management within the feature flag
 * management system. It handles user authentication, registration, administration, and lifecycle
 * management operations through a RESTful API interface.
 *
 * <p>The controller leverages the UserService to perform all business logic operations while
 * maintaining clean separation of concerns between the presentation layer and the business logic
 * layer. All endpoints include proper validation, security controls, and comprehensive error
 * handling.
 *
 * <p>Key functionalities: - User registration and account creation - User authentication and JWT
 * token management - Administrative user creation and management - User retrieval and profile
 * management - User deletion and account lifecycle management - Health check for service monitoring
 *
 * <p>Security features: - Role-based access control (RBAC) with ADMIN and USER roles - JWT
 * token-based authentication for stateless security - Method-level security with @PreAuthorize
 * annotations - Input validation using Jakarta Bean Validation - Secure password handling and
 * compromise validation
 *
 * <p>Authentication and authorization: - Public endpoints: health check, user registration, login -
 * User-level endpoints: user retrieval, profile management - Admin-level endpoints: admin
 * registration, user deletion - JWT tokens for secure session management
 *
 * <p>API design principles: - RESTful endpoint design with proper HTTP methods - Consistent
 * response formats and status codes - Comprehensive Swagger/OpenAPI documentation - Validation and
 * error handling at all levels - Stateless design for scalability
 *
 * <p>Integration points: - UserService for business logic execution - Spring Security for
 * authentication and authorization - JWT utilities for token management - Validation framework for
 * input sanitization
 *
 * <p>Swagger documentation: - Each endpoint is annotated with @Operation and @ApiResponse for clear
 * API documentation - Example responses provided for clarity - Grouped under the authentication
 * base path for organization - Custom annotations for common response patterns - Accessible via
 * Swagger UI for easy exploration and testing {@link SwaggerApiResponses} for standard responses,
 * error handling, and documentation {@link ApiResponse} for individual endpoint responses, success
 * messages, and error details {@link Operation} for endpoint summaries and detailed descriptions
 * {@link Timed} for performance monitoring and metrics collection
 */
@RestController
@RequestMapping("${api.auth}")
public class UserControllerImp {
  private final UserService userService;

  public UserControllerImp(UserService userService) {
    this.userService = userService;
  }

  /**
   * Health check endpoint for service monitoring and status verification.
   *
   * @return "OK" string indicating the service is functioning correctly
   */
  @GetMapping("/health")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Service is healthy",
      content = @Content(mediaType = "application/json"))
  @Operation(
      summary = "Verifies the health status of the service",
      description = "Returns 'OK' if the service is functioning correctly.")
  @Timed(value = "health_check", description = "Time taken to perform health check")
  public String healthCheck() {
    return "OK";
  }

  /**
   * Registers a new user account in the feature flag management system.
   *
   * @param userDTO the user registration data containing username, email, and password
   * @return ResponseEntity with HTTP 201 status and JWT token for immediate authentication
   * @throws UserAlreadyExistsException if username or email already exists in the system
   * @throws ValidationException if the provided data fails validation requirements
   */
  @PostMapping("/register")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "201",
      description = "User registered successfully",
      content =
          @Content(
              mediaType = "text/plain",
              schema =
                  @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")))
  @Operation(
      summary = "Registers a new user",
      description = "Registers a new user with the provided data and returns the user's JWT token.")
  public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDTO userDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDTO));
  }

  /**
   * Authenticates a user and provides access to the feature flag management system.
   *
   * @param loginRequestDto the authentication credentials containing username and password
   * @return ResponseEntity with HTTP 200 status and JWT token for authenticated access
   * @throws AuthenticationException if credentials are invalid or authentication fails
   * @throws AccountLockedException if the account is locked due to security reasons
   */
  @PostMapping("/login")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Authentication successful",
      content =
          @Content(
              mediaType = "text/plain",
              schema =
                  @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")))
  @Operation(
      summary = "Logs in a user",
      description =
          "Logs in a user with the provided credentials and returns the user's JWT token.")
  public ResponseEntity<String> logginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
    return ResponseEntity.ok(userService.loginUser(loginRequestDto));
  }

  /**
   * Registers a new administrative user with elevated privileges.
   *
   * @param userDTO the admin user registration data with username, email, and secure password
   * @return ResponseEntity with HTTP 201 status and JWT token with administrative privileges
   * @throws UserAlreadyExistsException if admin username or email already exists
   * @throws SecurityException if admin creation is not authorized
   * @throws ValidationException if admin registration data fails validation
   */
  @PostMapping("/register/admin")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "201",
      description = "Admin user registered successfully",
      content =
          @Content(
              mediaType = "text/plain",
              schema =
                  @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")))
  @Operation(
      summary = "Registers a new admin user",
      description =
          "Registers a new admin user with the provided information and returns a JWT token.")
  public ResponseEntity<String> registerAdmin(@Valid @RequestBody UserRequestDTO userDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerAdmin(userDTO));
  }

  /**
   * Retrieves a comprehensive list of all users in the feature flag management system.
   *
   * <p>Accessible by users with ADMIN or USER roles.
   *
   * @return ResponseEntity containing a list of UserDTO objects with user information
   * @throws SecurityException if the user lacks appropriate permissions
   * @throws AuthenticationException if authentication token is invalid or expired
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/users")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "List of all users retrieved successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDTO.class, type = "array")))
  @Operation(
      summary = "Retrieves all users",
      description = "Retrieves a comprehensive list of all users in the system.")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  /**
   * Retrieves a specific user account by their email address.
   *
   * <p>Accessible by users with ADMIN or USER roles.
   *
   * @param email the email address of the user to retrieve
   * @return ResponseEntity containing the UserDTO with user profile information
   * @throws UserNotFoundException if no user exists with the specified email
   * @throws SecurityException if the user lacks appropriate permissions
   * @throws AuthenticationException if authentication token is invalid
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/user/{email}")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "User retrieved successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDTO.class)))
  @Operation(
      summary = "Retrieves a user by email",
      description = "Retrieves the user profile associated with the specified email address.")
  public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  /**
   * Permanently deletes a user account from the feature flag management system.
   *
   * <p>Accessible only by users with ADMIN role.
   *
   * @param userId the UUID of the user account to delete
   * @return ResponseEntity with HTTP 204 (No Content) status indicating successful deletion
   * @throws UserNotFoundException if the specified user UUID does not exist
   * @throws SecurityException if the requesting user lacks ADMIN permissions
   * @throws IllegalArgumentException if the UUID format is invalid
   * @throws AuthenticationException if authentication token is invalid or expired
   */
  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  @SwaggerApiResponses
  @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content)
  @Operation(
      summary = "Deletes a user by UUID",
      description = "Permanently deletes the user account associated with the specified UUID.")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    UUID uuid = UUID.fromString(userId);
    userService.deleteUser(uuid);
    return ResponseEntity.noContent().build();
  }
}
