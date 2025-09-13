package com.equipo01.featureflag.featureflag.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.FeatureMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;
import com.equipo01.featureflag.featureflag.repository.specifications.FeatureSpecification;
import com.equipo01.featureflag.featureflag.service.UserService;
import com.equipo01.featureflag.featureflag.util.BaseLinkBuilder;
import com.equipo01.featureflag.featureflag.util.LinksDtoBuilder;
import com.equipo01.featureflag.featureflag.util.PageRequestFactory;
import com.equipo01.featureflag.featureflag.util.QueryParamBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class FeatureServiceImplTest {

  @Mock private FeatureRepository featureRepository;
  @Mock private FeatureMapper featureMapper;
  @Mock private UserService userService;
  @Mock private BaseLinkBuilder baseLinkBuilder;
  @Mock private LinksDtoBuilder linksDtoBuilder;
  @Mock private PageRequestFactory pageRequestFactory;
  @Mock private QueryParamBuilder queryParamBuilder;
  @Mock private FeatureSpecification featureSpecification;
  @InjectMocks private FeatureServiceImpl featureServiceImpl;

  @Test
  public void testCreateFeature() {
    FeatureRequestDto featureRequestDto = mock(FeatureRequestDto.class);
    Feature feature = mock(Feature.class);
    Feature expectedFeature = mock(Feature.class);

    when(featureMapper.toEntity(featureRequestDto)).thenReturn(feature);
    when(featureRepository.save(feature)).thenReturn(expectedFeature);
    featureServiceImpl.createFeature(featureRequestDto);
    verify(featureMapper).toEntity(featureRequestDto);
    verify(featureRepository).save(feature);
  }

  @Test
  public void testGetFeatureById() {
    String featureId = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID featureUUID = UUID.fromString(featureId);
    Feature expectedFeature = mock(Feature.class);
    when(featureRepository.findById(featureUUID)).thenReturn(Optional.of(expectedFeature));
    featureServiceImpl.getFeatureById(featureId);
    verify(featureRepository).findById(featureUUID);
  }

  @Test
  public void testGetFeatures() {
    int page = 0;
    int size = 10;
    Boolean enabledByDefault = true;
    String name = "Test Feature";

    Specification<Feature> spec = mock(Specification.class);
    when(featureSpecification.getFeatures(name, enabledByDefault)).thenReturn(spec);
    Page<Feature> featurePage = mock(Page.class);
    when(featureRepository.findAll(spec, pageRequestFactory.createPageRequest(page, size)))
        .thenReturn(featurePage);
    featureServiceImpl.getFeatures(name, enabledByDefault, page, size);
    verify(featureSpecification).getFeatures(name, enabledByDefault);
  }

  @Test
  public void testIsPageEmpty_throwsFeatureFlagException() {
    Page<Feature> featurePage = mock(Page.class);
    when(featurePage.isEmpty()).thenReturn(true);
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.isPageEmpty(featurePage));
    assertEquals(MessageError.FEATURES_NOT_FOUND.getStatus(), result.getStatus());
  }

  @Test
  public void testExistsByName_throwsFeatureFlagException() {
    String featureName = "Test Feature";
    when(featureRepository.existsByName(featureName)).thenReturn(Boolean.TRUE);
    FeatureFlagException result =
        assertThrows(
            FeatureFlagException.class, () -> featureServiceImpl.existsByName(featureName));
    verify(featureRepository).existsByName(featureName);
    assertEquals(MessageError.FEATURE_ALREADY_EXISTS.getMessage(), result.getMessage());
  }

  @Test
  public void testExistsByName() {
    String featureName = "Test Feature";
    when(featureRepository.existsByName(featureName)).thenReturn(Boolean.FALSE);
    boolean exists = featureServiceImpl.existsByName(featureName);
    verify(featureRepository).existsByName(featureName);
    assertFalse(exists);
  }

  @Test
  public void testExistsById_throwsFeatureFlagException() {
    UUID featureUUID = mock(UUID.class);
    when(featureRepository.existsById(featureUUID)).thenReturn(Boolean.TRUE);
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.existsById(featureUUID));
    verify(featureRepository).existsById(featureUUID);
    assertEquals(MessageError.FEATURE_ALREADY_EXISTS.getMessage(), result.getMessage());
  }

  @Test
  public void testFindById_throwsFeatureFlagException() {
    String featureId = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID featureUUID = UUID.fromString(featureId);
    when(featureRepository.findById(featureUUID)).thenReturn(Optional.empty());
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.findById(featureUUID));
    verify(featureRepository).findById(featureUUID);
    assertEquals(MessageError.FEATURE_NOT_FOUND.getMessage(), result.getMessage());
  }

  @Test
  public void testFindById() {
    String featureId = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID featureUUID = UUID.fromString(featureId);
    Feature expectedFeature = mock(Feature.class);
    when(featureRepository.findById(featureUUID)).thenReturn(Optional.of(expectedFeature));
    Feature result = featureServiceImpl.findById(featureUUID);
    verify(featureRepository).findById(featureUUID);
    assertNotNull(result);
  }

  @Test
  public void testFindByName_throwsFeatureFlagException() {
    String featureName = "Test Feature";
    when(featureRepository.findByName(featureName)).thenReturn(Optional.empty());
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.findByName(featureName));
    verify(featureRepository).findByName(featureName);
    assertEquals(MessageError.FEATURE_NOT_FOUND.getMessage(), result.getMessage());
  }

  @Test
  public void testFindByName() {
    String featureName = "Test Feature";
    Feature expectedFeature = mock(Feature.class);
    when(featureRepository.findByName(featureName)).thenReturn(Optional.of(expectedFeature));
    Feature result = featureServiceImpl.findByName(featureName);
    verify(featureRepository).findByName(featureName);
    assertNotNull(result);
  }

  @Test
  public void testCheckFeatureIsActive_returnFalseByEnvironment() {
    String featureName = "Test Feature";
    Environment environment = Environment.DEV;
    UUID clientID = UUID.randomUUID();

    Feature feature = mock(Feature.class);
    when(featureRepository.findByName(featureName)).thenReturn(Optional.of(feature));

    FeatureConfig featureConfig =
        FeatureConfig.builder().environment(Environment.PROD).enabled(true).build();
    var featureConfigList = List.of(featureConfig);
    when(feature.getConfigs()).thenReturn(featureConfigList);

    Boolean isActive = featureServiceImpl.checkFeatureIsActive(featureName, clientID, environment);
    verify(featureRepository).findByName(featureName);
    verify(feature).getConfigs();
    assertFalse(isActive);
  }

  @Test
  public void testCheckFeatureIsActive_returnFalseByEnabled() {
    String featureName = "Test Feature";
    Environment environment = Environment.DEV;
    UUID clientID = UUID.randomUUID();

    Feature feature = mock(Feature.class);
    when(featureRepository.findByName(featureName)).thenReturn(Optional.of(feature));

    FeatureConfig featureConfig =
        FeatureConfig.builder().environment(environment).enabled(false).build();
    var featureConfigList = List.of(featureConfig);
    when(feature.getConfigs()).thenReturn(featureConfigList);

    Boolean isActive = featureServiceImpl.checkFeatureIsActive(featureName, clientID, environment);
    verify(featureRepository).findByName(featureName);
    verify(feature).getConfigs();
    assertFalse(isActive);
  }

  @Test
  public void testCheckFeatureIsActive_returnTrue() {
    String featureName = "Test Feature";
    Environment environment = Environment.DEV;
    UUID clientID = UUID.randomUUID();

    Feature feature = mock(Feature.class);
    when(featureRepository.findByName(featureName)).thenReturn(Optional.of(feature));

    FeatureConfig featureConfig =
        FeatureConfig.builder().environment(environment).enabled(true).build();
    var featureConfigList = List.of(featureConfig);
    when(feature.getConfigs()).thenReturn(featureConfigList);

    Boolean isActive = featureServiceImpl.checkFeatureIsActive(featureName, clientID, environment);
    verify(featureRepository).findByName(featureName);
    verify(feature).getConfigs();
    assertTrue(isActive);
  }
}
