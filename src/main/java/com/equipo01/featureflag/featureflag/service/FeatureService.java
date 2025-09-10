package com.equipo01.featureflag.featureflag.service;

import java.util.List;
import java.util.UUID;

import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.model.Feature;

/**
 * Service interface for managing feature flags.
 * Provides methods for checking existence, retrieving, creating, and listing
 * features.
 */
public interface FeatureService {

    /**
     * Checks if a feature flag exists by its name.
     *
     * @param name the name of the feature flag
     * @return true if a feature flag with the given name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Retrieves a feature flag by its UUID.
     *
     * @param featureId the UUID of the feature flag
     * @return the Feature entity if found
     */
    Feature findById(UUID featureId);

    /**
     * Creates a new feature flag.
     *
     * @param requestDto the data for the new feature flag
     * @return the created feature flag as a response DTO
     */
    FeatureResponseDto createFeature(FeatureRequestDto requestDto);

    /**
     * Retrieves all feature flags.
     *
     * @return a list of feature flag response DTOs
     */
    List<FeatureResponseDto> getAllFeatures();

    /**
     * Retrieves a feature flag by its ID as a string.
     *
     * @param featureId the ID of the feature flag as a string
     * @return the feature flag as a response DTO
     */
    FeatureResponseDto getFeatureById(UUID featureId);

    Boolean checkFeatureIsActive(String nameFeature, UUID clientID, Environment environment);
}