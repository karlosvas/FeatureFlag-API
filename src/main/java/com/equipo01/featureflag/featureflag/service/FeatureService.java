package com.equipo01.featureflag.featureflag.service;

import java.util.List;
import java.util.UUID;
import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;

/**
 * Service for feature flags management.
 * 
 * This interface defines the main operations to create, query and manage
 * feature flags in the system. Feature flags allow enabling or disabling
 * application functionalities dynamically without the need to redeploy code.
 * 
 */
public interface FeatureService {

    /**
     * Creates a new feature flag in the system.
     * 
     * This method validates that the feature flag name is unique before creating
     * the new functionality. If a feature flag with the same name already exists,
     * an exception is thrown.
     * 
     * @param requestDto The feature flag data to create, including name,
     *                   description and initial state
     * @return FeatureResponseDto with the created feature flag data,
     *         including the auto-generated ID
     * @throws FeatureAlreadyExistsException if a feature flag with the same name
     *                                       already exists
     * @throws IllegalArgumentException      if the input data is invalid
     */
    FeatureResponseDto createFeature(FeatureRequestDto requestDto);

    /**
     * Retrieves all feature flags registered in the system.
     * 
     * This method recovers the complete list of available feature flags,
     * including both active and inactive ones. Results are returned as
     * response DTOs to avoid exposing internal entities.
     * 
     * @return List of FeatureResponseDto with all feature flags in the system.
     *         If no feature flags are registered, returns an empty list.
     */
    List<FeatureResponseDto> getAllFeatures();

    /**
     * Searches and retrieves a specific feature flag by its unique identifier.
     * 
     * This method searches for a feature flag using its UUID. If the feature flag
     * exists, it returns its complete data in a response DTO. If not found,
     * it throws a specific exception.
     * 
     * @param featureId The unique identifier (UUID) of the feature flag to search.
     *                  Cannot be null.
     * @return FeatureResponseDto with the complete data of the found feature flag
     * @throws FeatureNotFoundException if no feature flag exists with the provided
     *                                  ID
     * @throws IllegalArgumentException if featureId is null
     */
    FeatureResponseDto getFeatureById(UUID featureId);

}
