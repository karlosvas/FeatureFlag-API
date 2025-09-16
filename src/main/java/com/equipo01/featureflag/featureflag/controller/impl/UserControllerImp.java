package com.equipo01.featureflag.featureflag.controller.impl;

import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
 * This controller provides comprehensive endpoints for user management within the
 * feature flag management system. It handles user authentication, registration,
 * administration, and lifecycle management operations through a RESTful API interface.
 * 
 * The controller leverages the UserService to perform all business logic operations
 * while maintaining clean separation of concerns between the presentation layer and
 * the business logic layer. All endpoints include proper validation, security controls,
 * and comprehensive error handling.
 * 
 * Key functionalities:
 * - User registration and account creation
 * - User authentication and JWT token management
 * - Administrative user creation and management
 * - User retrieval and profile management
 * - User deletion and account lifecycle management
 * - Health check for service monitoring
 * 
 * Security features:
 * - Role-based access control (RBAC) with ADMIN and USER roles
 * - JWT token-based authentication for stateless security
 * - Method-level security with @PreAuthorize annotations
 * - Input validation using Jakarta Bean Validation
 * - Secure password handling and compromise validation
 * 
 * Authentication and authorization:
 * - Public endpoints: health check, user registration, login
 * - User-level endpoints: user retrieval, profile management
 * - Admin-level endpoints: admin registration, user deletion
 * - JWT tokens for secure session management
 * 
 * API design principles:
 * - RESTful endpoint design with proper HTTP methods
 * - Consistent response formats and status codes
 * - Comprehensive Swagger/OpenAPI documentation
 * - Validation and error handling at all levels
 * - Stateless design for scalability
 * 
 * Integration points:
 * - UserService for business logic execution
 * - Spring Security for authentication and authorization
 * - JWT utilities for token management
 * - Validation framework for input sanitization
 * 
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
   * 
   */
  @GetMapping("/health")
  @SwaggerApiResponses
  @Operation(
      summary = "Verifies the health status of the service",
      description = "Returns 'OK' if the service is functioning correctly.")
  public String healthCheck() {
    return "OK";
  }

  /**
   * Registers a new user account in the feature flag management system.
   * 
   * @param userDTO the user registration data containing username, email, and password
   * @return ResponseEntity with HTTP 201 status and JWT token for immediate authentication
   * 
   * @throws UserAlreadyExistsException if username or email already exists in the system
   * @throws ValidationException if the provided data fails validation requirements
   * 
   */
  @PostMapping("/register")
  @SwaggerApiResponses
  @Operation(
      summary = "Registers a new user",
      description =
          "Registers a new user with the provided data and returns the user's JWT token.")
  public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDTO userDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDTO));
  }

  /**
   * Authenticates a user and provides access to the feature flag management system.
   * 
   * @param loginRequestDto the authentication credentials containing username and password
   * @return ResponseEntity with HTTP 200 status and JWT token for authenticated access
   * 
   * @throws AuthenticationException if credentials are invalid or authentication fails
   * @throws AccountLockedException if the account is locked due to security reasons
   * 
   */
  @PostMapping("/login")
  @SwaggerApiResponses
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
   * 
   * @throws UserAlreadyExistsException if admin username or email already exists
   * @throws SecurityException if admin creation is not authorized
   * @throws ValidationException if admin registration data fails validation
   * 
   */
  @PostMapping("/register/admin")
  @SwaggerApiResponses
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
   * Accessible by users with ADMIN or USER roles.
   * 
   * @return ResponseEntity containing a list of UserDTO objects with user information
   * 
   * @throws SecurityException if the user lacks appropriate permissions
   * @throws AuthenticationException if authentication token is invalid or expired
   * 
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/users")
  @SwaggerApiResponses
  @Operation(
      summary = "Retrieves all users",
      description = "Retrieves a comprehensive list of all users in the system.")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  /**
   * Retrieves a specific user account by their email address.
   * 
    * Accessible by users with ADMIN or USER roles.
   * 
   * @param email the email address of the user to retrieve
   * @return ResponseEntity containing the UserDTO with user profile information
   * 
   * @throws UserNotFoundException if no user exists with the specified email
   * @throws SecurityException if the user lacks appropriate permissions
   * @throws AuthenticationException if authentication token is invalid
   * 
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/user/{email}")
  @SwaggerApiResponses
  @Operation(
      summary = "Retrieves a user by email",
      description = "Retrieves the user profile associated with the specified email address.")
  public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  /**
   * Permanently deletes a user account from the feature flag management system.
   * 
   * Accessible only by users with ADMIN role.
   * 
   * @param userId the UUID of the user account to delete
   * @return ResponseEntity with HTTP 204 (No Content) status indicating successful deletion
   * 
   * @throws UserNotFoundException if the specified user UUID does not exist
   * @throws SecurityException if the requesting user lacks ADMIN permissions
   * @throws IllegalArgumentException if the UUID format is invalid
   * @throws AuthenticationException if authentication token is invalid or expired
   * 
   */
  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  @SwaggerApiResponses
  @Operation(
      summary = "Deletes a user by UUID",
      description = "Permanently deletes the user account associated with the specified UUID.")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    UUID uuid = UUID.fromString(userId);
    userService.deleteUser(uuid);
    return ResponseEntity.noContent().build();
  }
}
