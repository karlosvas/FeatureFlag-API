package com.equipo01.featureflag.featureflag.exception;

import java.util.UUID;
import lombok.Getter;

/**
 * Excepción lanzada cuando no se encuentra una feature.
 * 
 * Atributos:
 * - featureId: ID de la feature no encontrada.
 *
 * Anotaciones:
 * - {@link Getter}: Genera automáticamente el método getter para el atributo featureId.
 * - {@link UUID}: Representa un identificador único para la feature.
 * - {@link RuntimeException}: Clase base para excepciones que pueden ser lanzadas durante la ejecución del programa.
 */
@Getter
public class FeatureNotFoundException extends RuntimeException {
    
    private final UUID featureId;

    public FeatureNotFoundException(UUID featureId) {
        super("Feature with ID '" + featureId + "' not found.");
        this.featureId = featureId;
    }
}
