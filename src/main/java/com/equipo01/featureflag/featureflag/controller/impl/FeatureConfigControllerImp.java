package com.equipo01.featureflag.featureflag.controller.impl;

import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.controller.FeatureConfigController;
import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.service.FeatureConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
 * REST Controller implementation for managing feature configurations in the Feature Flag system.
 *
 * <p>This controller provides endpoints for CRUD operations on feature configurations, including
 * creating, retrieving, updating, and deleting feature flag configurations. It also provides
 * functionality to enable or disable features dynamically.
 *
 * <p>The controller implements role-based access control with the following permissions:
 *
 * <ul>
 *   <li><strong>ADMIN:</strong> Full access to all operations including deletion and permission
 *       testing
 *   <li><strong>USER:</strong> Access to create and retrieve feature configurations
 *   <li><strong>Public:</strong> Access to retrieve all features and enable/disable functionality
 * </ul>
 *
 * <p>All endpoints return appropriate HTTP status codes and structured response bodies. Error
 * responses follow the standard {@code ErrorDto} format for consistency.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.configurations}")
public class FeatureConfigControllerImp implements FeatureConfigController {
  /** Service layer dependency for handling feature configuration business logic. */
  private final FeatureConfigService featureConfigService;

  /**
   * Enables or disables a specific feature configuration.
   *
   * @param featureConfigId the UUID string of the feature configuration to modify
   * @param enable {@code true} to enable the feature, {@code false} to disable it
   * @return ResponseEntity containing a list of updated feature configurations
   * @throws IllegalArgumentException if the featureConfigId is not a valid UUID format
   */
  @PutMapping("/enable-disable")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Feature configuration enabled/disabled successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = FeatureConfigResponseDto.class, type = "array")))
  @Operation(
      summary = "Enable or disable a specific feature configuration",
      description =
          "Toggles the enabled state of a feature configuration based on the provided parameters.")
  public ResponseEntity<List<FeatureConfigResponseDto>> setFeatureEnabledOrDisabled(
      @RequestParam(name = "featureConfigId", required = true) String featureConfigId,
      @RequestParam(name = "enable", required = true) boolean enable) {
    UUID featureConfigUUID = UUID.fromString(featureConfigId);
    return featureConfigService.enableOrDisableFeature(featureConfigUUID, enable);
  }

  /**
   * Creates a new feature configuration.
   *
   * @param requestDto the feature configuration data transfer object containing the configuration
   *     details
   * @return ResponseEntity with HTTP 201 status and the created feature configuration
   * @throws jakarta.validation.ConstraintViolationException if the request body validation fails
   */
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "201",
      description = "Feature configuration created successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = FeatureConfigResponseDto.class)))
  @Operation(
      summary = "Create a new feature configuration",
      description =
          "Creates a new feature configuration with the provided details and returns the created configuration.")
  public ResponseEntity<FeatureConfigResponseDto> createFeatureConfig(
      @Valid @RequestBody FeatureConfigRequestDto requestDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(featureConfigService.createFeatureConfig(requestDto));
  }

  /**
   * Retrieves feature configurations by their unique identifier.
   *
   * @param id the UUID string of the feature configuration to retrieve
   * @return ResponseEntity containing a list of feature configurations matching the ID
   * @throws IllegalArgumentException if the id is not a valid UUID format
   * @throws FeatureNotFoundException if no feature configuration exists with the given ID
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Feature configurations retrieved successfully by ID",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = FeatureConfigResponseDto.class, type = "array")))
  @Operation(
      summary = "Retrieve feature configurations by ID",
      description = "Fetches feature configurations matching the provided unique identifier.")
  public ResponseEntity<List<FeatureConfigResponseDto>> getFeatureByID(
      @PathVariable("id") String id) {
    UUID uuid = UUID.fromString(id);
    return ResponseEntity.ok(featureConfigService.getFeatureByID(uuid));
  }

  /**
   * Retrieves all available feature configurations.
   *
   * @return ResponseEntity containing a list of all feature configurations
   */
  @GetMapping
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "All feature configurations retrieved successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = FeatureConfigResponseDto.class, type = "array")))
  @Operation(
      summary = "Retrieve all feature configurations",
      description = "Fetches a list of all available feature configurations.")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<List<FeatureConfigResponseDto>> getAllFeatures() {
    return ResponseEntity.ok(featureConfigService.getAllFeatures());
  }

  /**
   * Deletes a specific feature configuration.
   *
   * @param featureConfigId the UUID string of the feature configuration to delete
   * @return ResponseEntity with HTTP 204 No Content status upon successful deletion
   * @throws IllegalArgumentException if the featureConfigId is not a valid UUID format
   * @throws FeatureNotFoundException if no feature configuration exists with the given ID
   * @throws AccessDeniedException if the user does not have ADMIN role
   */
  @DeleteMapping("/{featureConfigId}")
  @PreAuthorize("hasRole('ADMIN')")
  @SwaggerApiResponses
  @ApiResponse(responseCode = "204", description = "Feature configuration deleted successfully")
  @Operation(
      summary = "Delete a specific feature configuration",
      description = "Removes the feature configuration identified by the provided UUID.")
  public ResponseEntity<Void> deleteFeatureConfig(@PathVariable String featureConfigId) {
    UUID uuid = UUID.fromString(featureConfigId);
    featureConfigService.deleteFeatureConfig(uuid);
    return ResponseEntity.noContent().build();
  }
}
