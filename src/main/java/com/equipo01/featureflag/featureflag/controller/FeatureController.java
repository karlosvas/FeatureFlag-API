package com.equipo01.featureflag.featureflag.controller;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface FeatureController {
  /**
   * Creates a new feature flag.
   *
   * @param requestDto data of the feature flag to create
   * @return the created feature flag with HTTP status 201
   */
  public ResponseEntity<FeatureResponseDto> createFeature(
      @Valid @RequestBody FeatureRequestDto requestDto);

  /**
   * Obtains a paginated list of all feature flags, with optional filters by name and
   * enabled status.
   *
   * @param name optional filter by name (partial match)
   * @param enabledByDefault optional filter by enabled status
   * @param page page number for pagination (default: 0)
   * @param size page size for pagination (default: 10)
   * @return a paginated list of feature flags matching the applied filters
   */
  public ResponseEntity<GetFeatureResponseDto> getFeatures(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "enabled", required = false) Boolean enabledByDefault,
      @RequestParam(value = "page", defaultValue = "0", required = false)
          @Min(value = 0, message = "Page must be at least 0")
          Integer page,
      @RequestParam(value = "size", defaultValue = "10", required = false)
          @Min(value = 1, message = "Size must be at least 1")
          Integer size);

  /**
   * Retrieves details of a specific feature flag by its UUID.
   *
   * @param featureId the UUID of the feature flag
   * @return the feature flag details
   */
  public ResponseEntity<FeatureResponseDto> getFeature(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format")
          String featureId);

  /**
   * Checks if a specific feature flag is active for a given client and environment.
   * 
   * @param nameFeature the name of the feature flag to evaluate
   * @param clientID the unique identifier of the client requesting the feature status
   * @param environment the target environment (dev, staging, prod, etc.)
   * @return true if the feature is active for the given context, false otherwise
   * 
   * @apiNote This endpoint is designed for high-frequency usage by client applications
   *          and should have minimal latency impact on application performance
   */
  public ResponseEntity<Boolean> checkFeatureIsActive(
      @RequestParam String nameFeature,
      @RequestParam String clientID,
      @RequestParam String environment);

  /**
   * Updates feature flag configuration for specific clients or environments.
   * 
   * @param id the UUID of the feature flag to update (must be valid UUID format)
   * @param action the update action to perform (enable, disable, configure, etc.)
   * @param toggleRequestDto the configuration data for the update operation
   * @return the updated feature configuration or confirmation of the operation
   * 
   * @throws IllegalArgumentException if the UUID format is invalid
   * @throws FeatureFlagException if the feature is not found or action is unsupported
   * 
   * @apiNote This endpoint supports both immediate and scheduled feature updates
   *          depending on the configuration provided in the request DTO
   */
  public ResponseEntity<?> updateFeatureForClientOrEnvironment(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format")
          String id,
      @PathVariable String action,
      @RequestBody FeatureToggleRequestDto toggleRequestDto);

  /**
   * Deletes a feature flag from the system.
   * 
   * @param id the UUID of the feature flag to delete
   * @return empty response with HTTP status 204 (No Content) on successful deletion
   * 
   * @throws FeatureFlagException if the feature is not found or deletion is not allowed
   * @throws SecurityException if the user lacks permission to delete the feature
   * 
   * @apiNote This operation is irreversible. Consider disabling the feature first
   *          to test impact before permanent deletion
   */
  public ResponseEntity<Void> deleteFeature(@PathVariable String id);
}
