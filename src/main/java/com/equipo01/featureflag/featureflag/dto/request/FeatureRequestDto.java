package com.equipo01.featureflag.featureflag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for feature flag creation requests.
 * 
 * This DTO represents the data required to create a new feature flag in the
 * system. It encapsulates the essential information needed to define a feature
 * flag including its identification, description, and default behavior settings.
 * Feature flags created with this DTO serve as the foundation for dynamic
 * feature control throughout the application lifecycle.
 * 
 * The DTO supports various feature flag use cases:
 * - New feature rollouts with controlled activation
 * - A/B testing scenarios with configurable defaults
 * - Emergency feature toggles for incident response
 * - Beta feature management for limited user groups
 * - Legacy feature deprecation with gradual deactivation
 * 
 * All fields include comprehensive validation to ensure data integrity and
 * prevent the creation of malformed feature flags that could affect system
 * stability or feature evaluation logic.
 * 
 * Key characteristics:
 * - Comprehensive validation for all required fields
 * - Descriptive naming conventions for easy feature identification
 * - Boolean default behavior for clear feature state definition
 * - Builder pattern support for flexible object construction
 * - Swagger/OpenAPI integration for automatic documentation
 * 
 * JSON structure example:
 * <pre>
 * {
 *   "name": "dark_mode",
 *   "description": "Enables dark mode theme in the application",
 *   "enabledByDefault": true
 * }
 * </pre>
 * 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a new feature flag")
public class FeatureRequestDto {

  /**
   * Name of the feature flag.
   */
  @Schema(description = "Name of the feature flag", example = "dark_mode")
  @NotBlank(message = "Feature name is required")
  private String name;

  /**
   * Description of the feature flag.
   */
  @Schema(
      description = "Description of the feature flag",
      example = "Enables dark mode theme in the application")
  @NotBlank(message = "Feature description is required")
  private String description;

  /**
   * Indicates if the feature is enabled by default.
   */
  @Schema(description = "Indicates if the feature is enabled by default", example = "true")
  @NotNull(message = "Feature enabledByDefault status is required")
  private Boolean enabledByDefault;
}
