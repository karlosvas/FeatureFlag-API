package com.equipo01.featureflag.featureflag.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.FeatureMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import com.equipo01.featureflag.featureflag.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the
 * {@link com.equipo01.featureflag.featureflag.service.FeatureService}
 * interface.
 * <p>
 * Provides business logic for managing feature flags, including creation,
 * retrieval,
 * and validation of feature flag entities.
 * </p>
 *
 * <p>
 * This service uses a {@link FeatureRepository} for persistence and a
 * {@link FeatureMapper}
 * for converting between entities and DTOs.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;
    private final FeatureMapper featureMapper;
    private final UserService userService;

    /**
     * Creates a new feature flag.
     * <p>
     * Validates that the feature name does not already exist. If it does, throws a
     * {@link FeatureFlagException}.
     * Otherwise, saves the new feature and returns its DTO representation.
     * </p>
     *
     * @param requestDto the data for the new feature flag
     * @return the created feature flag as a response DTO
     * @throws FeatureFlagException if a feature with the same name already exists
     */
    @Override
    @Transactional
    public FeatureResponseDto createFeature(FeatureRequestDto requestDto) {

        existsByName(requestDto.getName());

        log.info("Creating new feature entity: {}", requestDto);
        Feature feature = featureMapper.toEntity(requestDto);
        Feature savedFeature = featureRepository.save(feature);

        log.info("Feature entity created successfully: {}", feature);
        return featureMapper.toDto(savedFeature);

    }

    /**
     * Retrieves all feature flags.
     *
     * @return a list of feature flag response DTOs
     */
    @Transactional(readOnly = true)
    public List<FeatureResponseDto> getAllFeatures() {
        return featureMapper.toDtoList(featureRepository.findAll());
    }

    /**
     * Retrieves a feature flag by its ID as a string.
     * <p>
     * Converts the string ID to a UUID and fetches the corresponding feature.
     * </p>
     *
     * @param featureId the ID of the feature flag as a string
     * @return the feature flag as a response DTO
     * @throws FeatureFlagException if the feature is not found
     */
    @Transactional(readOnly = true)
    public FeatureResponseDto getFeatureById(UUID featureId) {
        Feature feature = findById(featureId);
        return featureMapper.toDto(feature);
    }

    /**
     * Checks if a feature flag exists by its name.
     * <p>
     * If a feature with the given name exists, throws a
     * {@link FeatureFlagException}.
     * </p>
     *
     * @param name the name of the feature flag
     * @return false if the feature does not exist
     * @throws FeatureFlagException if the feature already exists
     */
    @Override
    public boolean existsByName(String name) {
        if (featureRepository.existsByName(name)) {
            log.warn("Feature name already exists: {}", name);
            throw new FeatureFlagException(
                    MessageError.FEATURE_ALREADY_EXISTS.getStatus(),
                    MessageError.FEATURE_ALREADY_EXISTS.getMessage(),
                    MessageError.FEATURE_ALREADY_EXISTS.getDescription());
        }
        return false;
    }

    /**
     * Retrieves a feature flag by its UUID.
     *
     * @param featureId the UUID of the feature flag
     * @return the Feature entity if found
     * @throws FeatureFlagException if the feature is not found
     */
    @Override
    public Feature findById(UUID featureId) {
        Optional<Feature> feature = featureRepository.findById(featureId);

        if(feature.isEmpty()){
            throw new FeatureFlagException(
                MessageError.FEATURE_NOT_FOUND.getStatus(),
                MessageError.FEATURE_NOT_FOUND.getMessage(),
                MessageError.FEATURE_NOT_FOUND.getDescription());
        }
        
        return feature.get();
    }

    public Feature findByName(String featureName) {
        Optional<Feature> feature = featureRepository.findByName(featureName);

        if(feature.isEmpty()){
            throw new FeatureFlagException(
                MessageError.FEATURE_NOT_FOUND.getStatus(),
                MessageError.FEATURE_NOT_FOUND.getMessage(),
                MessageError.FEATURE_NOT_FOUND.getDescription());
        }
        
        return feature.get();
    }

    @Transactional(readOnly = true)
    public Boolean checkFeatureIsActive(String nameFeature, UUID clientID, Environment environment) {
        // Check if the feature exists; throws an error if not found
        Feature feature = this.findByName(nameFeature);

        // Check if the client with the given ID exists; throws an error if not found
        userService.existsByClientID(clientID);

        // Get all feature configurations
        List<FeatureConfig> featureConfigList = feature.getConfigs();

        // Iterate through feature configs and look for the configuration for the given environment
        for (FeatureConfig config : featureConfigList)
            if (config.getEnvironment() == environment && config.isEnabled())
                return true;

        // If the environment was not found or was found but not enabled, return false
        return false;
    }
}