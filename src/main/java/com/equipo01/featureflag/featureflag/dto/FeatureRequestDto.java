package com.equipo01.featureflag.featureflag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for requesting the creation of a new feature flag.
 * Includes the data needed to create a feature flag in the system.
 * 
 * Annotations used:
 * - {@link Data} Automatically generates the get, set, toString, equals, and hashCode methods.
 * - {@link Builder} Generates the builder pattern for the class.
 * - {@link NoArgsConstructor} Generates a no-arguments constructor.
 * - {@link AllArgsConstructor} Generates a constructor with all arguments.
 * 
 * Attributes
 * - name: Name of the feature flag.
 * - description: Description of the feature flag.
 * - enabledByDefault: Indicates whether the feature is enabled by default.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for requesting the creation of a new feature flag.")
public class FeatureRequestDto {
    
    @Schema(description = "Name of th feature flag", example = "dark_mode")
    @NotBlank(message = "Feature name is required")
    private String name;

    @Schema(description = "Description of the feature flag", example = "Enables dark mode in the application")
    @NotBlank(message = "Feature description is required")
    private String description;

    @Schema(description = "Indicates whether the feature is enabled by default", example = "true")
    @NotNull(message = "Feature enabledByDefault status is required")
    @Builder.Default
    private boolean enabledByDefault = true;
}
