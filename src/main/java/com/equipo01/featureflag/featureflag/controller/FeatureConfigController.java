package com.equipo01.featureflag.featureflag.controller;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * REST API Controller interface for managing feature flag configurations.
 *
 * <p>This controller provides comprehensive endpoints for managing feature flag configurations
 * across different environments and clients. It handles the creation, retrieval, modification, and
 * deletion of feature configurations that control how feature flags behave in various contexts.
 *
 * <p>Feature configurations represent the specific settings and parameters that determine how a
 * feature flag operates in different environments (development, staging, production) and for
 * different clients. This includes environment-specific overrides, client-specific settings, and
 * conditional activation rules.
 *
 * <p>Key responsibilities: - Configuration lifecycle management (create, read, update, delete) -
 * Environment-specific feature configuration - Client-specific feature overrides - Bulk
 * configuration operations for efficiency - Permission-based access control for configuration
 * changes - Feature enablement/disablement toggles
 *
 * <p>Configuration management features: - Environment isolation (dev, staging, prod configurations)
 * - Client-specific overrides and customizations - Conditional activation based on context - Bulk
 * operations for managing multiple configurations - Real-time configuration updates without
 * deployment - Configuration versioning and rollback capabilities
 *
 * <p>Security and permissions: - Role-based access control for configuration management - Audit
 * logging for all configuration changes - Permission validation for sensitive operations - Secure
 * configuration parameter handling
 *
 * <p>Integration points: - Feature flag evaluation engine for runtime decisions - Environment
 * management system for context-aware configurations - Client management system for client-specific
 * overrides - Monitoring and analytics for configuration performance tracking
 *
 * <p>Performance considerations: - Optimized for high-frequency configuration reads - Caching
 * strategies for frequently accessed configurations - Batch operations for bulk configuration
 * management - Minimal latency impact on feature flag evaluation
 */
public interface FeatureConfigController {

  /**
   * Creates a new feature flag configuration.
   *
   * @param requestDto the configuration data for the new feature config
   * @return the created feature configuration with assigned ID and settings
   */
  ResponseEntity<FeatureConfigResponseDto> createFeatureConfig(FeatureConfigRequestDto requestDto);

  /**
   * Retrieves all feature configurations associated with a specific feature flag.
   *
   * @param id the unique identifier of the feature flag
   * @return a list of all configurations associated with the specified feature flag
   */
  ResponseEntity<List<FeatureConfigResponseDto>> getFeatureByID(String id);

  /**
   * Retrieves all feature configurations across all feature flags in the system.
   *
   * @return a complete list of all feature configurations in the system
   */
  ResponseEntity<List<FeatureConfigResponseDto>> getAllFeatures();

  /**
   * Enables or disables a specific feature configuration.
   *
   * @param featureConfigId the unique identifier of the feature configuration to toggle
   * @param enable true to enable the configuration, false to disable it
   * @return a list of updated configurations affected by the toggle operation
   */
  ResponseEntity<List<FeatureConfigResponseDto>> setFeatureEnabledOrDisabled(
      String featureConfigId, boolean enable);

  /**
   * Deletes a specific feature configuration from the system.
   *
   * @param id the unique identifier of the feature configuration to delete
   * @return empty response with HTTP status 204 (No Content) on successful deletion
   * @throws FeatureConfigException if the configuration is not found or deletion is not allowed
   * @throws SecurityException if the user lacks permission to delete the configuration
   */
  ResponseEntity<Void> deleteFeatureConfig(String id);
}
