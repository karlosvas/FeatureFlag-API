package com.equipo01.featureflag.featureflag.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;

import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.model.enums.Environment;

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

    boolean existsById(UUID id);

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
     * Retrieves a feature flag by its ID as a string.
     *
     * @param featureId the ID of the feature flag as a string
     * @return the feature flag as a response DTO
     */
    FeatureResponseDto getFeatureById(String featureId);

    Boolean checkFeatureIsActive(String nameFeature, UUID clientID, Environment environment);

    /**
     * Retrieves a paginated list of feature flags, optionally filtered by name and
     * enabled status.
     *
     * @param name             optional name filter (partial match)
     * @param enabledByDefault optional enabled status filter
     * @param page             the page number to retrieve (0-based)
     * @param size             the number of items per page
     * @return a paginated response DTO containing the list of feature flags
     */
    GetFeatureResponseDto getFeatures(String name, Boolean enabledByDefault,
            Integer page, Integer size);

    /**
     * Validates if the given page of features is empty.
     * If empty, throws a FeatureFlagException.
     *
     * @param featurePage the page of features to check
     * @throws FeatureFlagException if the page is empty
     */
    void isPageEmpty(Page<Feature> featurePage);

        /**
         * Enables or disable a  feature for specifici client or environment.
         * This method will check if the feature identified by {@code featureId} exists,
         * then it will either create a new configuration or update an existing one
         * inf{@link FeatureConfig} with {@code enabled = true} based on the provided
         * {@code requestDto} containing {@code clientId} and/or {@code environment}.
         * 
         * 
         * @param featureId  the UUID of the feature to be enabled.
         * @param requestDto a DTO containing the clientID and/or environment where the
         *                   feature sholud be enabled.
         * @param  enabled a boolean indicating wether to enable (true) or disable (false) the feature
         * @throws FeatureFlagException if the feature does not exist or if the
         *                              requestDto is invalid.
         * 
         */
    void updateFeatureForClientOrEnvironment(UUID featureId, FeatureToggleRequestDto toggleRequestDto, boolean enabled);

}