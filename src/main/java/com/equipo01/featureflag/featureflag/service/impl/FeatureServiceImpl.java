package com.equipo01.featureflag.featureflag.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.FeatureMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;
import com.equipo01.featureflag.featureflag.service.FeatureService;

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
    public FeatureResponseDto getFeatureById(String featureId) {
        UUID uuid = UUID.fromString(featureId);
        Feature feature = findById(uuid);
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
        return featureRepository.findById(featureId)
                .orElseThrow(() -> new FeatureFlagException(
                        MessageError.FEATURE_NOT_FOUND.getStatus(),
                        MessageError.FEATURE_NOT_FOUND.getMessage(),
                        MessageError.FEATURE_NOT_FOUND.getDescription()));
    }

}
