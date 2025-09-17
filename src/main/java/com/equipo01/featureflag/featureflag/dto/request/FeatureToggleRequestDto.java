package com.equipo01.featureflag.featureflag.dto.request;

import com.equipo01.featureflag.featureflag.model.enums.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for feature toggle evaluation requests.
 *
 * <p>This DTO represents the context information required to evaluate whether a feature flag should
 * be enabled or disabled for a specific scenario. It provides the necessary parameters for the
 * feature evaluation engine to determine the appropriate feature state based on configuration
 * hierarchies and business rules.
 *
 * <p>The DTO supports flexible feature evaluation by allowing both fields to be optional
 * (nullable), enabling various evaluation scenarios: - Global feature evaluation (no context
 * specified) - Environment-specific evaluation (environment only) - Client-specific evaluation
 * (clientId only) - Fully contextual evaluation (both environment and clientId)
 *
 * <p>Feature evaluation hierarchy typically follows: 1. Exact match: specific environment + client
 * configuration 2. Environment match: environment-specific configuration 3. Client match:
 * client-specific configuration 4. Fallback: feature's default enabled state
 *
 * <p>Key characteristics: - Optional fields for flexible evaluation contexts - Environment
 * enumeration for type safety - Client identification for multi-tenant support - Builder pattern
 * for convenient object construction
 *
 * <p>JSON structure examples:
 *
 * <pre>
 * Full context evaluation
 * {
 *   "clientId": "acme-corp",
 *   "environment": "PRODUCTION"
 * }
 *
 * Environment-only evaluation
 * {
 *   "clientId": null,
 *   "environment": "STAGING"
 * }
 *
 * Global evaluation
 * {
 *   "clientId": null,
 *   "environment": null
 * }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureToggleRequestDto {

  /** Client identifier for feature evaluation context. */
  private String clientId; // Nullable

  /** Environment context for feature evaluation. */
  private Environment environment; // Nullable
}
