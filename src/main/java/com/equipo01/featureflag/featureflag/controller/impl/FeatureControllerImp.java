package com.equipo01.featureflag.featureflag.controller.impl;

import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.controller.FeatureController;
import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller implementation for managing feature flags in the Feature Flag system.
 *
 * <p>This controller provides comprehensive endpoints for feature flag management including
 * creating, retrieving, updating, and deleting feature flags. It also supports dynamic feature
 * toggling for specific clients and environments, making it suitable for production feature
 * rollouts and A/B testing scenarios.
 *
 * <p>The controller implements role-based access control with the following permissions:
 *
 * <ul>
 *   <li><strong>ADMIN:</strong> Full access to all operations including deletion
 *   <li><strong>USER:</strong> Access to create, retrieve, and check feature flags
 *   <li><strong>Public:</strong> Access to feature toggle operations
 * </ul>
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>CRUD operations for feature flags
 *   <li>Paginated feature listing with filtering capabilities
 *   <li>Dynamic feature toggling per client/environment
 *   <li>Feature activation status checking
 *   <li>Comprehensive validation and error handling
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.features}")
public class FeatureControllerImp implements FeatureController {

  /** Service layer dependency for handling feature flag business logic. */
  private final FeatureService featureService;

  /**
   * Enables or disables a feature configuration for a specific client or environment.
   *
   * @param id the UUID of the feature to toggle (must be valid UUID format)
   * @param action the action to perform: "enable" or "disable"
   * @param toggleRequestDto the request data containing client or environment specifications
   * @return ResponseEntity with HTTP 204 No Content status upon successful operation
   * @throws IllegalArgumentException if the ID is not a valid UUID format
   * @throws FeatureNotFoundException if no feature exists with the given ID
   */
  @Override
  @PutMapping("/{id}/{action:(?:enable|disable)}")
  @SwaggerApiResponses
  @ApiResponse(responseCode = "204", description = "Feature configuration updated successfully")
  @Operation(
      summary = "Enable or disable a feature configuration for a specific client or environment",
      description =
          "Enables or disables a feature configuration for a specific client or environment based on the 'action' parameter. Supports gradual rollouts and A/B testing scenarios.")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<Void> updateFeatureForClientOrEnvironment(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format")
          String id,
      @PathVariable String action,
      @RequestBody FeatureToggleRequestDto toggleRequestDto) {

    UUID featureId = UUID.fromString(id);
    boolean enable = action.equalsIgnoreCase("enable");
    featureService.updateFeatureForClientOrEnvironment(featureId, toggleRequestDto, enable);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Creates a new feature flag in the system.
   *
   * @param requestDto the feature flag data transfer object containing the feature configuration
   * @return ResponseEntity with HTTP 201 status and the created feature flag details
   * @throws jakarta.validation.ConstraintViolationException if the request body validation fails
   * @throws FeatureAlreadyExistsException if a feature with the same name already exists
   */
  @PostMapping
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "201",
      description = "Feature flag created successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = FeatureResponseDto.class)))
  @Operation(
      summary = "Create a new feature flag",
      description =
          "Creates a new feature flag with the provided data and returns the created feature details. The feature will be available for environment-specific and client-specific configurations.")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<FeatureResponseDto> createFeature(
      @Valid @RequestBody FeatureRequestDto requestDto) {
    FeatureResponseDto responseDto = featureService.createFeature(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  /**
   * Retrieves a paginated list of feature flags with optional filtering.
   *
   * @param name optional filter by feature name (partial matching supported)
   * @param enabledByDefault optional filter by default enabled status
   * @param page the page number for pagination (0-based, default: 0)
   * @param size the number of items per page (minimum: 1, default: 10)
   * @return ResponseEntity containing paginated feature flags with metadata
   * @throws jakarta.validation.ConstraintViolationException if pagination parameters are invalid
   */
  @GetMapping
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Feature flags retrieved successfully with pagination",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = GetFeatureResponseDto.class)))
  @Operation(
      summary = "Retrieve all feature flags with pagination and filtering",
      description =
          "Returns a paginated list of all available feature flags with optional filtering by name and enabled status. Supports pagination for efficient data retrieval.")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<GetFeatureResponseDto> getFeatures(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "enabled", required = false) Boolean enabledByDefault,
      @RequestParam(value = "page", defaultValue = "0", required = false)
          @Min(value = 0, message = "Page must be at least 0")
          Integer page,
      @RequestParam(value = "size", defaultValue = "10", required = false)
          @Min(value = 1, message = "Size must be at least 1")
          Integer size) {

    GetFeatureResponseDto getFeatureResponseDto =
        featureService.getFeatures(name, enabledByDefault, page, size);
    return ResponseEntity.ok().body(getFeatureResponseDto);
  }

  /**
   * Retrieves the details of a specific feature flag by its UUID.
   *
   * @param featureId the UUID of the feature flag to retrieve (must be valid UUID format)
   * @return ResponseEntity containing the detailed feature flag information
   * @throws IllegalArgumentException if the featureId is not a valid UUID format
   * @throws FeatureNotFoundException if no feature flag exists with the given ID
   */
  @GetMapping("/{featureId}")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Feature flag found and retrieved successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = FeatureResponseDto.class)))
  @Operation(
      summary = "Retrieve a feature flag by its ID",
      description =
          "Returns the detailed information of a specific feature flag identified by its UUID, including configuration and status details.")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<FeatureResponseDto> getFeature(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format")
          String featureId) {
    return ResponseEntity.ok(featureService.getFeatureById(featureId));
  }

  /**
   * Checks if a feature is active for a specific client in a given environment.
   *
   * @param nameFeature the name of the feature to check
   * @param clientID the UUID string of the client for whom to check the feature
   * @param environment the environment name (DEVELOPMENT, STAGING, PRODUCTION)
   * @return ResponseEntity containing true if the feature is active, false otherwise
   * @throws IllegalArgumentException if clientID is not a valid UUID or environment is invalid
   * @throws FeatureNotFoundException if the specified feature does not exist
   */
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Feature activation status retrieved successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(type = "boolean", example = "true")))
  @Operation(
      summary = "Check if a feature is active for a client in a specific environment",
      description =
          "Returns true if the feature is active for the specified client in the given environment, false otherwise. Essential for runtime feature flag evaluation.")
  @GetMapping("/check")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<Boolean> checkFeatureIsActive(
      @RequestParam String nameFeature,
      @RequestParam String clientID,
      @RequestParam String environment) {
    Environment env = Environment.valueOf(environment);
    UUID uuid = UUID.fromString(clientID);
    Boolean isActive = featureService.checkFeatureIsActive(nameFeature, uuid, env);
    return ResponseEntity.ok(isActive);
  }

  /**
   * Permanently deletes a feature flag from the system.
   *
   * @param featureId the UUID string of the feature flag to delete
   * @return ResponseEntity with HTTP 204 No Content status upon successful deletion
   * @throws IllegalArgumentException if the featureId is not a valid UUID format
   * @throws FeatureNotFoundException if no feature flag exists with the given ID
   * @throws AccessDeniedException if the user does not have ADMIN role
   */
  @DeleteMapping("/{featureId}")
  @SwaggerApiResponses
  @ApiResponse(responseCode = "204", description = "Feature flag deleted successfully")
  @Operation(
      summary = "Delete a feature flag permanently",
      description =
          "Permanently removes a feature flag and all its associated configurations from the system. This operation is irreversible and requires administrative privileges.")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteFeature(@PathVariable String featureId) {
    UUID uuid = UUID.fromString(featureId);
    featureService.deleteFeature(uuid);
    return ResponseEntity.noContent().build();
  }

    /**
   * Test endpoint for verifying administrative permissions.
   *
   * @return ResponseEntity with a success message if the user has proper permissions
   * @throws AccessDeniedException if the user does not have ADMIN role
   */
  @GetMapping("/test")
  @PreAuthorize("hasRole('ADMIN')")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Admin permission test successful",
      content =
          @Content(
              mediaType = "text/plain",
              schema = @Schema(type = "string", example = "Test permission ok")))
  @Operation(
      summary = "Test endpoint for verifying ADMIN permissions",
      description = "Accessible only by users with ADMIN role to confirm permission settings.")
  public ResponseEntity<String> checkPermissionTest() {
    return ResponseEntity.ok("Test permission ok");
  }
}
