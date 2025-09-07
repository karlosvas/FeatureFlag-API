package com.equipo01.featureflag.featureflag.exception;

import java.util.UUID;

public class FeatureNotFoundException extends RuntimeException {
    
    private final UUID featureId;

    public FeatureNotFoundException(UUID featureId) {
        super("Feature with ID '" + featureId + "' not found.");
        this.featureId = featureId;
    }

    public UUID getFeatureId() {
        return featureId;
    }
    
}
