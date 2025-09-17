package com.equipo01.featureflag.featureflag.service.impl;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.FeatureMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;
import com.equipo01.featureflag.featureflag.repository.specifications.FeatureSpecification;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import com.equipo01.featureflag.featureflag.service.UserService;
import com.equipo01.featureflag.featureflag.util.BaseLinkBuilder;
import com.equipo01.featureflag.featureflag.util.LinksDtoBuilder;
import com.equipo01.featureflag.featureflag.util.PageRequestFactory;
import com.equipo01.featureflag.featureflag.util.QueryParamBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link com.equipo01.featureflag.featureflag.service.FeatureService}
 * interface.
 *
 * <p>Provides business logic for managing feature flags, including creation, retrieval, and
 * validation of feature flag entities.
 *
 * <p>This service uses a {@link FeatureRepository} for persistence and a {@link FeatureMapper} for
 * converting between entities and DTOs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureServiceImpl implements FeatureService {

  private final FeatureRepository featureRepository;
  private final FeatureMapper featureMapper;
  private final UserService userService;
  private final BaseLinkBuilder baseLinkBuilder;
  private final LinksDtoBuilder linksDtoBuilder;
  private final PageRequestFactory pageRequestFactory;
  private final QueryParamBuilder queryParamBuilder;
  private final FeatureSpecification featureSpecification;

  /**
   * Creates a new feature flag.
   *
   * <p>Validates that the feature name does not already exist. If it does, throws a {@link
   * FeatureFlagException}. Otherwise, saves the new feature and returns its DTO representation.
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
   * Retrieves a feature flag by its ID as a string.
   *
   * <p>Converts the string ID to a UUID and fetches the corresponding feature.
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
   * Retrieves a paginated list of feature flags based on optional filters.
   *
   * <p>Supports filtering by name (partial match) and enabledByDefault status. Returns a paginated
   * response with links for navigation.
   *
   * @param name optional name filter (partial match)
   * @param enabledByDefault optional enabledByDefault filter
   * @param page the page number to retrieve (0-based)
   * @param size the number of items per page
   * @return a paginated response DTO containing the list of features and links
   * @throws FeatureFlagException if no features are found with the provided filters
   */
  @Transactional(readOnly = true)
  public GetFeatureResponseDto getFeatures(
      String name, Boolean enabledByDefault, Integer page, Integer size) {
    Specification<Feature> spec = featureSpecification.getFeatures(name, enabledByDefault);
    var pageRequest = pageRequestFactory.createPageRequest(page, size);

    Page<Feature> featurePage = featureRepository.findAll(spec, pageRequest);

    isPageEmpty(featurePage);
    var pathSegments = queryParamBuilder.buildQueryFeature(name, enabledByDefault);
    var basePath = baseLinkBuilder.createBaseLink(pathSegments);
    var links = linksDtoBuilder.createLinksDto(featurePage, basePath);

    return GetFeatureResponseDto.builder()
        .features(featureMapper.toDtoList(featurePage.getContent()))
        .links(links)
        .build();
  }

  /**
   * Checks if the provided page of features is empty.
   *
   * <p>If the page is empty, throws a {@link FeatureFlagException} indicating that no features were
   * found.
   *
   * @param featurePage the page of features to check
   * @throws FeatureFlagException if the page is empty
   */
  @Override
  public void isPageEmpty(Page<Feature> featurePage) {
    if (featurePage.isEmpty()) {
      log.warn("No features found with the provided filters");
      throw new FeatureFlagException(
          MessageError.FEATURES_NOT_FOUND.getStatus(),
          MessageError.FEATURES_NOT_FOUND.getMessage(),
          MessageError.FEATURES_NOT_FOUND.getDescription());
    }
  }

  /**
   * Checks if a feature flag exists by its name.
   *
   * <p>If a feature with the given name exists, throws a {@link FeatureFlagException}.
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

  public boolean existsById(UUID id) {
    if (featureRepository.existsById(id)) {
      log.warn("Feature id already exists: {}", id);
      throw new FeatureFlagException(
          MessageError.FEATURE_ALREADY_EXISTS.getStatus(),
          MessageError.FEATURE_ALREADY_EXISTS.getMessage(),
          MessageError.FEATURE_ALREADY_EXISTS.getDescription());
    }
    ;
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

    if (feature.isEmpty()) {
      throw new FeatureFlagException(
          MessageError.FEATURE_NOT_FOUND.getStatus(),
          MessageError.FEATURE_NOT_FOUND.getMessage(),
          MessageError.FEATURE_NOT_FOUND.getDescription());
    }

    return feature.get();
  }

  public Feature findByName(String featureName) {
    Optional<Feature> feature = featureRepository.findByName(featureName);

    if (feature.isEmpty()) {
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

    // Iterate through feature configs and look for the configuration for the given
    // environment
    for (FeatureConfig config : featureConfigList) {
      if (config.getEnvironment() == environment && config.getEnabled()) return true;
    }
    // If the environment was not found or was found but not enabled, return false
    return false;
  }

  /**
   * Deletes a feature flag by its UUID. If the feature with the given ID does not exist, throws a
   * {@link FeatureFlagException}.
   *
   * @param featureId the UUID of the feature to be deleted.
   * @throws FeatureFlagException if the feature does not exist. {@link Transactional} annotation
   *     ensures that the delete operation is executed within a transaction.
   */
  @Transactional
  public void deleteFeature(UUID featureId) {
    if (featureRepository.existsById(featureId)) {
      featureRepository.deleteById(featureId);
    } else {
      throw new FeatureFlagException(
          MessageError.FEATURE_NOT_FOUND.getStatus(),
          MessageError.FEATURE_NOT_FOUND.getMessage(),
          MessageError.FEATURE_NOT_FOUND.getDescription());
    }
  }

  /**
   * Enables or disable a feature for specifici client or environment. This method will check if the
   * feature identified by {@code featureId} exists, then it will either create a new configuration
   * or update an existing one inf{@link FeatureConfig} with {@code enabled = true} based on the
   * provided {@code requestDto} containing {@code clientId} and/or {@code environment}.
   *
   * @param featureId the UUID of the feature to be enabled.
   * @param requestDto a DTO containing the clientID and/or environment where the feature sholud be
   *     enabled.
   * @param enabled a boolean indicating wether to enable (true) or disable (false) the feature
   * @throws FeatureFlagException if the feature does not exist or if the requestDto is invalid.
   */
  @Override
  public void updateFeatureForClientOrEnvironment(
      UUID featureId, FeatureToggleRequestDto toggleRequestDto, boolean enable) {
    // 1. Retrives the feature by its ID, if not found, throws an exception
    Feature feature = this.findById(featureId);
    // 2. Validates the request DTO (must contain at least clientId or environment)
    validateFeatureToggleRequest(toggleRequestDto);
    // 3. Get the list of configurations associates with this feature
    List<FeatureConfig> featureConfigList = feature.getConfigs();
    // 4. Filter configurations that match the request conditions:
    // - If clientId is null, ignore it, otherwise compare with FeatureConfig
    // clientId
    // - If environment is null, ignore it, otherwise compare with FeatureConfig
    // environment
    List<FeatureConfig> targetConfig =
        featureConfigList.stream()
            .filter(
                fc -> {
                  boolean clientIdMatch =
                      toggleRequestDto.getClientId() == null
                          || toggleRequestDto.getClientId().equals(fc.getClientId());
                  boolean environmentMatch =
                      toggleRequestDto.getEnvironment() == null
                          || toggleRequestDto.getEnvironment() == fc.getEnvironment();
                  return clientIdMatch && environmentMatch;
                })
            .toList();

    if (!targetConfig.isEmpty()) {
      targetConfig.forEach(fc -> fc.setEnabled(enable));
      featureRepository.save(feature);
    } else {
      throw new FeatureFlagException(
          HttpStatus.NOT_FOUND,
          MessageError.FEATURE_NOT_FOUND.getMessage(),
          MessageError.FEATURES_NOT_FOUND.getDescription());
    }
  }

  /**
   * Validates the given DTO. The DTO must contain at least one of the following fields: {@code
   * clientId} or {@code environment} -if both are null, an exception will be thrown. -If {@code
   * clienId} is an empty String and {@code environment} is null, an exception will also be thrown.
   *
   * @param toggleRequestDto
   * @throws FeatureFlagException personalizada indicando el error
   */
  private void validateFeatureToggleRequest(FeatureToggleRequestDto toggleRequestDto) {
    boolean clientIdEmpty =
        toggleRequestDto.getClientId() == null || toggleRequestDto.getClientId().isBlank();
    boolean environmentEmpty = toggleRequestDto.getEnvironment() == null;

    if (clientIdEmpty && environmentEmpty) {
      throw new FeatureFlagException(
          MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getStatus(),
          MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getMessage(),
          MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getDescription());
    }
  }
}
