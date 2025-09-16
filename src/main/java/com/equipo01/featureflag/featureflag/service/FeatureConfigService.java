package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

/**
 * Service interface for managing feature flag configurations.
 * 
 * This interface defines operations for managing feature configurations that control
 * how feature flags behave in different environments, for specific clients, or under
 * particular conditions. Feature configurations provide the granular control needed
 * to enable/disable features dynamically without code changes.
 * 
 * The service handles:
 * - Creating new feature configurations
 * - Retrieving configurations by feature ID or all configurations
 * - Enabling/disabling specific feature configurations
 * - Deleting feature configurations when no longer needed
 * 
 * Feature configurations typically include:
 * - Environment-specific settings (dev, staging, prod)
 * - Client-specific overrides
 * - Conditional activation rules
 * - Rollout percentages for gradual feature releases
 * 
 */
public interface FeatureConfigService {
  
  /**
   * Creates a new feature configuration with the specified parameters.
   * 
   * @param requestDto The configuration data including environment, client ID,
   *                   enabled status, and any additional parameters for the feature
   * @return FeatureConfigResponseDto containing the created configuration with
   *         generated ID and any computed values
   * @throws IllegalArgumentException if the request data is invalid
   * @throws FeatureNotFoundException if the referenced feature does not exist
   * @throws ConfigurationConflictException if a conflicting configuration already exists
   */
  public FeatureConfigResponseDto createFeatureConfig(FeatureConfigRequestDto requestDto);

  /**
   * Retrieves all configurations associated with a specific feature.
   * 
   * @param id The unique identifier of the feature for which to retrieve configurations
   * @return List of FeatureConfigResponseDto containing all configurations for the feature.
   *         Returns an empty list if no configurations exist for the feature.
   * @throws FeatureNotFoundException if no feature exists with the provided ID
   * @throws IllegalArgumentException if the ID is null or invalid
   */
  public List<FeatureConfigResponseDto> getFeatureByID(UUID id);

  /**
   * Retrieves all feature configurations in the system.
   * 
   * @return List of FeatureConfigResponseDto containing all feature configurations.
   *         Returns an empty list if no configurations exist in the system.
   */
  public List<FeatureConfigResponseDto> getAllFeatures();

  /**
   * Enables or disables a specific feature configuration.
   * 
   * @param featureConfigId The unique identifier of the feature configuration to modify
   * @param enable true to enable the feature configuration, false to disable it
   * @return ResponseEntity containing a list of FeatureConfigResponseDto with updated
   *         configuration states. The response includes HTTP status indicating success or failure.
   * @throws FeatureConfigNotFoundException if no configuration exists with the provided ID
   * @throws IllegalArgumentException if the featureConfigId is null or invalid
   */
  public ResponseEntity<List<FeatureConfigResponseDto>> enableOrDisableFeature(
      UUID featureConfigId, boolean enable);

  /**
   * Permanently deletes a feature configuration from the system.
   * 
   * Note: Deleting a feature configuration does not affect the base feature flag,
   * only the specific configuration rules for environments, clients, or conditions.
   * 
   * @param id The unique identifier of the feature configuration to delete
   * @throws FeatureConfigNotFoundException if no configuration exists with the provided ID
   * @throws IllegalArgumentException if the ID is null or invalid
   * @throws ConfigurationInUseException if the configuration is currently being referenced
   *         and cannot be safely deleted
   */
  public void deleteFeatureConfig(UUID id);
}
