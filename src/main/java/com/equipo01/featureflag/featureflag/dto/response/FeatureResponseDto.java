package com.equipo01.featureflag.featureflag.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for feature flag response data.
 *
 * <p>This DTO represents the complete information about a feature flag that is returned to API
 * clients when querying feature flag data. It encapsulates the core properties of a feature flag
 * including its identification, metadata, and default behavior configuration.
 *
 * <p>The DTO serves multiple purposes: - Provides a clean API response structure without exposing
 * internal entities - Maintains data consistency across different API endpoints - Supports feature
 * flag discovery and management operations - Enables client-side feature flag decision making
 *
 * <p>Feature flags represented by this DTO can be used for: - Gradual feature rollouts and A/B
 * testing - Environment-specific feature control - Emergency feature toggles - Beta feature
 * management - Legacy feature deprecation
 *
 * <p>The DTO includes Swagger/OpenAPI annotations for automatic API documentation generation,
 * ensuring that clients have comprehensive information about the response structure and expected
 * data types.
 *
 * <p>JSON structure example:
 *
 * <pre>
 * {
 *   "id": "123e4567-e89b-12d3-a456-426614174000",
 *   "name": "dark_mode",
 *   "description": "Enables dark mode theme in the application",
 *   "enabledByDefault": true
 * }
 * </pre>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "DTO for feature flag response data.")
public class FeatureResponseDto implements Serializable {

  /**
   * Unique identifier of the feature flag.
   *
   * <p>This UUID serves as the primary key for the feature flag and enables clients to reference
   * this specific feature for configuration operations, status checks, or management actions. The
   * ID is automatically generated when the feature is created and remains constant throughout its
   * lifecycle.
   */
  @Schema(
      description = "Unique identifier of the feature flag",
      example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  /** Name of the feature flag. */
  @Schema(description = "Name of the feature flag", example = "dark_mode")
  private String name;

  /** Description of the feature flag. */
  @Schema(
      description = "Description of the feature flag",
      example = "Enables dark mode theme in the application")
  private String description;

  /** Indicates whether the feature is enabled by default. */
  @Schema(description = "Indicates whether the feature is enabled by default", example = "true")
  private Boolean enabledByDefault;
}
