package com.equipo01.featureflag.featureflag.service.impl;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.FeatureConfigMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.repository.FeatureConfigRepository;
import com.equipo01.featureflag.featureflag.service.FeatureConfigService;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Implementation of the FeatureConfigService interface for managing feature flag configurations.
 *
 * <p>This service provides concrete implementations for all feature configuration operations
 * including creation, retrieval, modification, and deletion. It manages the relationship between
 * features and their specific configurations for different environments, clients, or conditional
 * scenarios.
 *
 * <p>The implementation handles: - Feature configuration CRUD operations with proper validation -
 * Entity-DTO mapping through dedicated mappers - Transactional integrity for data consistency -
 * Relationship management between features and their configurations - Error handling with custom
 * exceptions and meaningful messages
 *
 * <p>Key responsibilities: - Validate feature existence before creating configurations - Ensure
 * data consistency through transactional operations - Provide proper HTTP responses for API
 * endpoints - Handle edge cases like missing configurations gracefully
 */
@Service
@RequiredArgsConstructor
public class FeatureConfigServiceImpl implements FeatureConfigService {

  /** Mapper for converting between FeatureConfig entities and DTOs */
  private final FeatureConfigMapper featureConfigMapper;

  /** Repository for feature configuration data persistence operations */
  private final FeatureConfigRepository featureConfigRepository;

  /** Service for feature operations and validation */
  private final FeatureService featureService;

  /**
   * Enables or disables a specific feature configuration dynamically.
   *
   * @param featureConfigUUID The unique identifier of the feature configuration to modify
   * @param enable true to enable the configuration, false to disable it
   * @return ResponseEntity with HTTP 200 and updated configuration if found, or HTTP 404 if the
   *     configuration doesn't exist
   */
  public ResponseEntity<List<FeatureConfigResponseDto>> enableOrDisableFeature(
      UUID featureConfigUUID, boolean enable) {
    Optional<FeatureConfig> featureConfig = featureConfigRepository.findById(featureConfigUUID);

    if (featureConfig.isPresent()) {
      featureConfig.get().setEnabled(enable);
      featureConfigRepository.save(featureConfig.get());
      return ResponseEntity.ok(List.of(featureConfigMapper.toDto(featureConfig.get())));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Creates a new feature configuration with comprehensive validation and relationship management.
   *
   * @param requestDto The configuration request containing feature ID, environment, client
   *     settings, and enabled status
   * @return FeatureConfigResponseDto with the created configuration including generated ID
   * @throws FeatureNotFoundException if the referenced feature does not exist
   * @throws IllegalArgumentException if the feature ID format is invalid
   */
  public FeatureConfigResponseDto createFeatureConfig(FeatureConfigRequestDto requestDto) {
    FeatureConfig featureConfig = featureConfigMapper.toEntity(requestDto);

    UUID featureUUID = UUID.fromString(requestDto.getFeatureId());
    Feature feature = featureService.findById(featureUUID);
    featureConfig.setFeature(feature);

    featureConfig = featureConfigRepository.save(featureConfig);

    return featureConfigMapper.toDto(featureConfig);
  }

  /**
   * Retrieves a specific feature configuration by its unique identifier.
   *
   * @param id The unique identifier of the feature configuration to retrieve
   * @return List containing the found configuration DTO, or empty list if not found
   */
  public List<FeatureConfigResponseDto> getFeatureByID(UUID id) {
    return featureConfigRepository
        .findById(id)
        .map(featureConfigMapper::toDto)
        .map(List::of)
        .orElseGet(List::of);
  }

  /**
   * Retrieves all feature configurations from the system.
   *
   * @return List of FeatureConfigResponseDto containing all configurations in the system. Returns
   *     an empty list if no configurations exist.
   */
  public List<FeatureConfigResponseDto> getAllFeatures() {
    List<FeatureConfig> featureConfigs = featureConfigRepository.findAll();
    List<FeatureConfigResponseDto> responseDtos = featureConfigMapper.toDtoList(featureConfigs);
    return responseDtos;
  }

  /**
   * Permanently deletes a feature configuration from the system with validation.
   *
   * <p>Warning: This operation is irreversible. Once deleted, the configuration cannot be recovered
   * and will affect feature behavior immediately.
   *
   * @param id The unique identifier of the feature configuration to delete
   * @throws FeatureFlagException with FEATURE_CONFIG_NOT_FOUND details if the configuration doesn't
   *     exist
   */
  @Transactional
  public void deleteFeatureConfig(UUID id) {
    if (featureConfigRepository.existsById(id)) {
      featureConfigRepository.deleteById(id);
    } else {
      throw new FeatureFlagException(
          MessageError.FEATURE_CONFIG_NOT_FOUND.getStatus(),
          MessageError.FEATURE_CONFIG_NOT_FOUND.getMessage(),
          MessageError.FEATURE_CONFIG_NOT_FOUND.getDescription());
    }
  }
}
