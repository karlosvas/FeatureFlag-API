package com.equipo01.featureflag.featureflag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de creación de una nueva feature flag.
 * Incluye los datos necesarios para crear una feature flag en el sistema.
 * 
 * Anotaciones utilizadas:
 * - {@link Data} Genera los métodos get, set, toString, equals y hashCode automáticamente.
 * - {@link Builder} Genera el patrón builder para la clase.
 * - {@link NoArgsConstructor} Genera un constructor sin argumentos.
 * - {@link AllArgsConstructor} Genera un constructor con todos los argumentos.
 * 
 * Atributos
 * - name: Nombre de la feature flag.
 * - description: Descripción de la feature flag.
 * - enabledByDefault: Indica si la feature está habilitada por defecto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a new feature flag")
public class FeatureRequestDto {

    @Schema(description = "Name of the feature flag", example = "dark_mode")
    @NotBlank(message = "Feature name is required")
    private String name;

    @Schema(description = "Description of the feature flag", example = "Activates dark mode in the application")
    @NotBlank(message = "Feature description is required")
    private String description;

    @Schema(description = "Indicates if the feature is enabled by default", example = "true")
    @NotNull(message = "Feature enabledByDefault status is required")
    @Builder.Default
    private boolean enabledByDefault = false;
}
