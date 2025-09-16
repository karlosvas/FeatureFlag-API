package com.equipo01.featureflag.featureflag.dto.request;

import com.equipo01.featureflag.featureflag.model.enums.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for feature configuration creation requests.
 *
 * <p>This DTO represents the data required to create a new feature configuration that defines how a
 * specific feature behaves in particular environments or for specific clients. It enables granular
 * control over feature flags beyond their default settings, supporting complex deployment scenarios
 * and client-specific customizations.
 *
 * <p>Feature configurations created with this DTO enable: - Environment-specific feature behavior
 * (dev, staging, production) - Client-specific feature overrides for multi-tenant scenarios -
 * Conditional feature activation based on business rules - Gradual feature rollouts and A/B testing
 * implementations
 *
 * <p>The DTO includes comprehensive validation to ensure data integrity and prevent the creation of
 * invalid configurations that could affect system stability or feature evaluation consistency.
 *
 * <p>Key characteristics: - Strict validation for all required fields - Environment enumeration for
 * type safety - String-based feature ID for flexible reference handling - Builder pattern support
 * for convenient object construction
 *
 * <p>JSON structure example:
 *
 * <pre>
 * {
 *   "environment": "PRODUCTION",
 *   "clientId": "acme-corp",
 *   "enabled": true,
 *   "featureId": "123e4567-e89b-12d3-a456-426614174000"
 * }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureConfigRequestDto {

  /** Environment where this configuration will be applied. */
  @NotNull(message = "environment is required")
  private Environment environment;

  /** Client identifier for which this configuration applies. */
  @NotBlank(message = "clientId is required")
  private String clientId;

  /** Whether the feature should be enabled for this configuration. */
  @NotNull(message = "enabled is required")
  private Boolean enabled;

  /** Identifier of the feature this configuration belongs to. */
  @NotBlank(message = "featureId is required")
  private String featureId;
}
