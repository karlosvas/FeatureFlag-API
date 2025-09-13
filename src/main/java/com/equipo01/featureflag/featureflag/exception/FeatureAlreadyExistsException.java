package com.equipo01.featureflag.featureflag.exception;

import lombok.Getter;

/**
 * Exception thrown when attempting to create a feature that already exists.
 * 
 * Attributes:
 * - featureName: Name of the feature that already exists.
 *
 * Annotations:
 * - {@link Getter}: Automatically generates the getter method for the featureName attribute.
 * - {@link RuntimeException}: Base class for exceptions that can be thrown during program execution.
 */
@Getter
public class FeatureAlreadyExistsException extends RuntimeException {
    
    private final String featureName;

    public FeatureAlreadyExistsException(String featureName) {
        super("Feature with name '" + featureName + "' already exists.");
        this.featureName = featureName;
    }
}
