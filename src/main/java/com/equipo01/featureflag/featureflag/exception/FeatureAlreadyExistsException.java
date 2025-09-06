package com.equipo01.featureflag.featureflag.exception;

public class FeatureAlreadyExistsException extends RuntimeException {
    
    private final String featureName;

    public FeatureAlreadyExistsException(String featureName) {
        super("Feature with name '" + featureName + "' already exists.");
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }
}
