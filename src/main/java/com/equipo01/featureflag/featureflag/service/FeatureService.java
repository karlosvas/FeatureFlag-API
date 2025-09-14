package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import java.util.UUID;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing feature flags. Provides methods for checking existence,
 * retrieving, creating, and listing features.
 */
public interface FeatureService {

  /**
   * Checks if a feature flag exists by its name.
   *
   * @param name the name of the feature flag
   * @return true if a feature flag with the given name exists, false otherwise
   */
  public boolean existsByName(String name);

  public boolean existsById(UUID id);

  /**
   * Retrieves a feature flag by its UUID.
   *
   * @param featureId the UUID of the feature flag
   * @return the Feature entity if found
   */
  public Feature findById(UUID featureId);

  /**
   * Creates a new feature flag.
   *
   * @param requestDto the data for the new feature flag
   * @return the created feature flag as a response DTO
   */
  public FeatureResponseDto createFeature(FeatureRequestDto requestDto);

  /**
   * Retrieves a feature flag by its ID as a string.
   *
   * @param featureId the ID of the feature flag as a string
   * @return the feature flag as a response DTO
   */
  public FeatureResponseDto getFeatureById(String featureId);

  public Boolean checkFeatureIsActive(String nameFeature, UUID clientID, Environment environment);

  /**
   * Retrieves a paginated list of feature flags, optionally filtered by name and enabled status.
   *
   * @param name optional name filter (partial match)
   * @param enabledByDefault optional enabled status filter
   * @param page the page number to retrieve (0-based)
   * @param size the number of items per page
   * @return a paginated response DTO containing the list of feature flags
   */
  public GetFeatureResponseDto getFeatures(
      String name, Boolean enabledByDefault, Integer page, Integer size);

  /**
   * Validates if the given page of features is empty. If empty, throws a FeatureFlagException.
   *
   * @param featurePage the page of features to check
   * @throws FeatureFlagException if the page is empty
   */
  public void isPageEmpty(Page<Feature> featurePage);

  public void deleteFeature(UUID featureId);
}
