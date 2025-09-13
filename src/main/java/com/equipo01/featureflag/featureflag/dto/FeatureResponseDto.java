package com.equipo01.featureflag.featureflag.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for the response of a feature flag.
 * Includes the data returned when querying a feature flag in the system.
 * 
 * Annotations used:
 * - {@link Data} Automatically generates the get, set, toString, equals, and hashCode methods.
 * - {@link Builder} Generates the builder pattern for the class.
 * - {@link NoArgsConstructor} Generates a no-arguments constructor.
 * - {@link AllArgsConstructor} Generates a constructor with all arguments.
 * 
 * Attributes
 * - id: Unique identifier of the feature flag.
 * - name: Name of the feature flag.
 * - description: Description of the feature flag.
 * - enabledByDefault: Indicates whether the feature is enabled by default.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "DTO for the response of a feature flag.")
public class FeatureResponseDto {

    @Schema(description = "Unique identifier of the feature flag", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the feature flag", example = "dark_mode")
    private String name;

    @Schema(description = "Description of the feature flag", example = "Enables dark mode in the application")
    private String description;

    @Schema(description = "Indicates whether the feature is enabled by default", example = "true")
    private boolean enabledByDefault;
}
