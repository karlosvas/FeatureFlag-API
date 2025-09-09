package com.equipo01.featureflag.featureflag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "DTO para la solicitud de creación de una nueva feature flag.")
public class FeatureRequestDto {
    
    @Schema(description = "Nombre de la feature flag", example = "dark_mode")
    @NotBlank(message = "Feature name is required")
    private String name;

    @Schema(description = "Descripción de la feature flag", example = "Activa el modo oscuro en la aplicación")
    @NotBlank(message = "Feature description is required")
    private String description;

    @Schema(description = "Indica si la feature está habilitada por defecto", example = "true")
    @NotNull(message = "Feature enabledByDefault status is required")
    @Builder.Default
    private boolean enabledByDefault = true;
}
