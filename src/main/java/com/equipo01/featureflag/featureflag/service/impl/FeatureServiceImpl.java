package com.equipo01.featureflag.featureflag.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureAlreadyExistsException;
import com.equipo01.featureflag.featureflag.exception.FeatureNotFoundException;
import com.equipo01.featureflag.featureflag.mapper.FeatureMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of the FeatureService interface for feature flags management.
 * 
 * This class provides the concrete implementation of all feature flag
 * operations
 * including creation, retrieval, and validation. It handles data persistence
 * through the repository layer, entity-DTO mapping, and comprehensive logging
 * for monitoring and debugging purposes.
 * 
 * The implementation includes:
 * - Transactional support for data integrity
 * - Duplicate name validation
 * - Exception handling for business logic violations
 * - Comprehensive logging for operational visibility
 */
@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    /** Repository for feature flag data persistence operations */
    private final FeatureRepository featureRepository;

    /** Mapper for converting between Feature entities and DTOs */
    private final FeatureMapper featureMapper;

    /** Logger instance for tracking service operations and debugging */
    private final Logger logger = Logger.getLogger(FeatureServiceImpl.class.getName());

    /**
     * Creates a new feature flag in the system with comprehensive validation and
     * logging.
     * 
     * This method performs the following operations:
     * 1. Logs the incoming request for audit purposes
     * 2. Validates that no feature flag with the same name already exists
     * 3. Converts the request DTO to a domain entity
     * 4. Persists the new feature flag to the database
     * 5. Returns the created feature flag as a response DTO
     * 
     * The method includes duplicate handling at both the application level
     * (via repository check) and database level (via
     * DataIntegrityViolationException).
     * 
     * @param requestDto The feature flag creation request containing name,
     *                   description, and initial state
     * @return FeatureResponseDto with the created feature flag data including
     *         generated ID and timestamps
     * @throws FeatureAlreadyExistsException   if a feature flag with the same name
     *                                         already exists
     * @throws DataIntegrityViolationException if database constraints are violated
     *                                         during persistence
     */
    @Override
    @Transactional
    public FeatureResponseDto createFeature(FeatureRequestDto requestDto) {
        logger.info("Feature request DTO: " + requestDto);
        try {

            if (featureRepository.existsByName(requestDto.getName())) {
                logger.warning("Feature with name '" + requestDto.getName() + "' already exists");
                throw new FeatureAlreadyExistsException(requestDto.getName());
            }

            Feature feature = featureMapper.toEntity(requestDto);
            logger.info("Feature entity created: " + feature);
            Feature savedFeature = featureRepository.save(feature);

            logger.info("Feature created successfully with id: " + savedFeature.getId());
            return featureMapper.toDto(savedFeature);
        } catch (DataIntegrityViolationException e) {
            logger.severe("Feature with name '" + requestDto.getName() + "' already exists");
            throw new FeatureAlreadyExistsException(requestDto.getName());
        }
    }

    /**
     * Retrieves all feature flags from the database and converts them to response
     * DTOs.
     * 
     * This method performs a read-only transaction to fetch all feature flags
     * from the repository, then maps each entity to its corresponding response DTO
     * using Java streams for efficient processing. The method ensures data
     * consistency through read-only transaction semantics.
     * 
     * @return List of FeatureResponseDto containing all feature flags in the
     *         system.
     *         Returns an empty list if no feature flags exist.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FeatureResponseDto> getAllFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(featureMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a specific feature flag by its unique identifier.
     * 
     * This method performs a read-only database lookup using the provided UUID.
     * If the feature flag is found, it's converted to a response DTO and returned.
     * If no feature flag exists with the given ID, a FeatureNotFoundException
     * is thrown with details about the missing resource.
     * 
     * @param featureId The unique identifier (UUID) of the feature flag to retrieve
     * @return FeatureResponseDto containing the complete feature flag data
     * @throws FeatureNotFoundException if no feature flag exists with the provided
     *                                  ID
     */
    @Override
    @Transactional(readOnly = true)
    public FeatureResponseDto getFeatureById(UUID featureId) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new FeatureNotFoundException(featureId));
        return featureMapper.toDto(feature);
    }

}
