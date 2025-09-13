package com.equipo01.featureflag.featureflag.exception;

import java.util.UUID;
import lombok.Getter;

/**
 * Exception thrown when a feature is not found.
 * 
 * Attributes:
 * - featureId: ID of the feature not found.
 *
 * Annotations:
 * - {@link Getter}: Automatically generates the getter method for the featureId attribute.
 * - {@link UUID}: Represents a unique identifier for the feature.
 * - {@link RuntimeException}: Base class for exceptions that can be thrown during program execution.
 */
@Getter
public class FeatureNotFoundException extends RuntimeException {
    
    private final UUID featureId;

    public FeatureNotFoundException(UUID featureId) {
        super("Feature with ID '" + featureId + "' not found.");
        this.featureId = featureId;
    }
}
