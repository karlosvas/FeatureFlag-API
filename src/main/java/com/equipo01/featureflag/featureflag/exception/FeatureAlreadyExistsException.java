package com.equipo01.featureflag.featureflag.exception;

import lombok.Getter;

/**
 * Excepción lanzada cuando se intenta crear una feature que ya existe.
 * 
 * Atributos:
 * - featureName: Nombre de la feature que ya existe.
 *
 * Anotaciones:
 * - {@link Getter}: Genera automáticamente el método getter para el atributo featureName.
 * - {@link RuntimeException}: Clase base para excepciones que pueden ser lanzadas durante la ejecución del programa.
 */
@Getter
public class FeatureAlreadyExistsException extends RuntimeException {
    
    private final String featureName;

    public FeatureAlreadyExistsException(String featureName) {
        super("Feature with name '" + featureName + "' already exists.");
        this.featureName = featureName;
    }
}
