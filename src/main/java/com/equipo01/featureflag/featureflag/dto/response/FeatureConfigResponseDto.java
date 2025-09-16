package com.equipo01.featureflag.featureflag.dto.response;

import com.equipo01.featureflag.featureflag.model.enums.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for feature configuration response data.
 *
 * <p>This DTO represents the complete information about a feature configuration that is returned to
 * API clients. It encapsulates the configuration settings that control how a specific feature
 * behaves in different environments or for specific clients, providing granular control over
 * feature flag behavior beyond the base feature settings.
 *
 * <p>Feature configurations enable: - Environment-specific feature control (dev, staging,
 * production) - Client-specific feature overrides - Conditional feature activation based on
 * external factors - Gradual feature rollouts and A/B testing scenarios
 *
 * <p>The DTO includes validation annotations to ensure data integrity when used in request
 * processing, though these are primarily enforced during entity creation and updates rather than
 * response serialization.
 *
 * <p>Key characteristics: - Immutable response structure for API consistency - Comprehensive
 * validation for data integrity - Environment enumeration for type safety - UUID identifiers for
 * global uniqueness - Builder pattern support for flexible construction
 *
 * <p>JSON structure example:
 *
 * <pre>
 * {
 *   "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
 *   "environment": "PRODUCTION",
 *   "clientId": "acme-corp",
 *   "enabled": true,
 *   "featureId": "f9e8d7c6-b5a4-3210-9876-543210fedcba"
 * }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureConfigResponseDto {

  /** Unique identifier for this feature configuration. */
  @NotNull(message = "id is required")
  private UUID id;

  /**
   * Environment where this configuration applies.
   *
   * <p>This field specifies the deployment environment for which this configuration is active. It
   * allows different feature behaviors across development, staging, and production environments,
   * enabling safe feature testing and gradual rollouts.
   */
  @NotBlank(message = "environment is required")
  private Environment environment;

  /** Client identifier for which this configuration applies. */
  @NotBlank(message = "clientId is required")
  private String clientId;

  /** Whether the feature is enabled for this specific configuration. */
  @NotNull(message = "enabled is required")
  private Boolean enabled;

  /** Identifier of the feature this configuration belongs to. */
  @NotNull(message = "featureId is required")
  private UUID featureId;
}
