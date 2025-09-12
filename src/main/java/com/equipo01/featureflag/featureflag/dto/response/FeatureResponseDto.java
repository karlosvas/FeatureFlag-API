package com.equipo01.featureflag.featureflag.dto.response;

import java.io.Serializable;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO para la respuesta de una feature flag.
 * Incluye los datos que se devuelven al consultar una feature flag en el sistema.
 * 
 * Anotaciones utilizadas:
 * - {@link Data} Genera los métodos get, set, toString, equals y hashCode automáticamente.
 * - {@link Builder} Genera el patrón builder para la clase.
 * - {@link NoArgsConstructor} Genera un constructor sin argumentos.
 * - {@link AllArgsConstructor} Genera un constructor con todos los argumentos.
 * 
 * Atributos
 * - id: Identificador único de la feature flag.
 * - name: Nombre de la feature flag.
 * - description: Descripción de la feature flag.
 * - enabledByDefault: Indica si la feature está habilitada por defecto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "DTO para la respuesta de una feature flag.")
public class FeatureResponseDto implements Serializable{
    
    @Schema(description = "Identificador único de la feature flag", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nombre de la feature flag", example = "dark_mode")
    private String name;

    @Schema(description = "Descripción de la feature flag", example = "Activa el modo oscuro en la aplicación")
    private String description;

    @Schema(description = "Indica si la feature está habilitada por defecto", example = "true")
    private Boolean enabledByDefault;
}
