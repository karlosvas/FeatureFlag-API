package com.equipo01.featureflag.featureflag.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class FeatureServiceImplTest {

  @Mock private FeatureRepository featureRepository;
  @Mock private FeatureMapper featureMapper;
  @Mock private UserService userService;
  @Mock private BaseLinkBuilder baseLinkBuilder;
  @Mock private LinksDtoBuilder linksDtoBuilder;
  @Mock private PageRequestFactory pageRequestFactory;
  @Mock private QueryParamBuilder queryParamBuilder;
  @Mock private FeatureSpecification featureSpecification;
  @InjectMocks private FeatureServiceImpl featureServiceImpl;

  private Feature feature;
  private FeatureConfig configDev;
  private FeatureConfig configProd;

  @BeforeEach
  void setUp() {
    // No es necesario llamar a openMocks cuando usamos
    // @ExtendWith(MockitoExtension.class)
    configDev =
        FeatureConfig.builder()
            .id(UUID.randomUUID())
            .environment(Environment.DEV)
            .clientId("clienteA")
            .enabled(false)
            .build();

    configProd =
        FeatureConfig.builder()
            .id(UUID.randomUUID())
            .environment(Environment.PROD)
            .clientId("clienteB")
            .enabled(false)
            .build();

    feature =
        Feature.builder()
            .id(UUID.randomUUID())
            .name("featureX")
            .description("Test Feature")
            .enabledByDefault(false)
            .configs(Arrays.asList(configDev, configProd))
            .build();

    configDev.setFeature(feature);
    configProd.setFeature(feature);
  }

  @Test
  void testEnabledFeatureForClientOrEnvironment_success() {
    when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));

    FeatureToggleRequestDto requestDto =
        FeatureToggleRequestDto.builder().clientId("clienteA").environment(null).build();

    featureServiceImpl.updateFeatureForClientOrEnvironment(feature.getId(), requestDto, true);

    assertTrue(configDev.getEnabled());
    assertFalse(configProd.getEnabled());

    verify(featureRepository, times(1)).save(feature);
  }

  @Test
  void testEnableFeatureForClientOrEnvironment_featureNotFound() {
    UUID randomId = UUID.randomUUID();
    when(featureRepository.findById(randomId)).thenReturn(Optional.empty());

    FeatureToggleRequestDto dto = FeatureToggleRequestDto.builder().clientId("clientA").build();

    assertThrows(
        FeatureFlagException.class,
        () -> featureServiceImpl.updateFeatureForClientOrEnvironment(randomId, dto, true));
  }

  @Test
  void testDisableFeatureForClientOrEnvironment_success() {
    // Mock repository to return the feature
    when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));

    // Crear DTO para deshabilitar configDev
    FeatureToggleRequestDto requestDto =
        FeatureToggleRequestDto.builder().clientId("clienteA").environment(null).build();

    // Primero habilitamos la feature para simular que estaba activa
    configDev.setEnabled(true);
    configProd.setEnabled(true);

    // Llamada al método de servicio
    featureServiceImpl.updateFeatureForClientOrEnvironment(feature.getId(), requestDto, false);

    // Verificaciones
    assertFalse(configDev.getEnabled()); // La configuración objetivo debe estar deshabilitada
    assertTrue(configProd.getEnabled()); // La otra configuración no debe cambiar

    // Verificar que se haya guardado la entidad Feature
    verify(featureRepository, times(1)).save(feature);
  }

  @Test
  void testDisableFeatureForClientOrEnvironment_featureNotFound() {
    UUID randomId = UUID.randomUUID();
    when(featureRepository.findById(randomId)).thenReturn(Optional.empty());

    FeatureToggleRequestDto dto = FeatureToggleRequestDto.builder().clientId("clientA").build();

    assertThrows(
        FeatureFlagException.class,
        () -> featureServiceImpl.updateFeatureForClientOrEnvironment(randomId, dto, false));
  }

  @Test
  void testDisableFeatureForClientOrEnvironment_invalidRequest() {
    // Mock repository
    when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));

    // DTO inválido (ambos clientId y environment null)
    FeatureToggleRequestDto invalidDto =
        FeatureToggleRequestDto.builder().clientId(null).environment(null).build();

    assertThrows(
        FeatureFlagException.class,
        () ->
            featureServiceImpl.updateFeatureForClientOrEnvironment(
                feature.getId(), invalidDto, false));
  }

  @Test
  void validateFeatureToggleRequest_throwsException_whenClientIdAndEnvironmentAreNull() {
    when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));
    // Crear DTO con clientId y environment nulos
    FeatureToggleRequestDto invalidDto =
        FeatureToggleRequestDto.builder().clientId(null).environment(null).build();

    FeatureFlagException ex =
        assertThrows(
            FeatureFlagException.class,
            () -> {
              featureServiceImpl.updateFeatureForClientOrEnvironment(
                  feature.getId(), invalidDto, true);
            });

    assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getMessage(), ex.getMessage());
    assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getDescription(), ex.getDescription());
    assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getStatus(), ex.getStatus());
  }

  @Test
  void validateFeatureToggleRequest_throwsException_whenClientIdAndEnvironmentAreEmpty() {
    when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));
    FeatureToggleRequestDto invalidDto =
        FeatureToggleRequestDto.builder().clientId(" ").environment(null).build();

    FeatureFlagException ex =
        assertThrows(
            FeatureFlagException.class,
            () -> {
              featureServiceImpl.updateFeatureForClientOrEnvironment(
                  feature.getId(), invalidDto, false);
            });

    assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getMessage(), ex.getMessage());
    assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getDescription(), ex.getDescription());
    assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getStatus(), ex.getStatus());
  }

  @Test
  void testCreateFeature() {
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
  void testGetFeatureById() {
    String featureId = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID featureUUID = UUID.fromString(featureId);
    Feature expectedFeature = mock(Feature.class);
    when(featureRepository.findById(featureUUID)).thenReturn(Optional.of(expectedFeature));
    featureServiceImpl.getFeatureById(featureId);
    verify(featureRepository).findById(featureUUID);
  }

  @Test
  void testGetFeatures() {
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
  void testIsPageEmpty_throwsFeatureFlagException() {
    Page<Feature> featurePage = mock(Page.class);
    when(featurePage.isEmpty()).thenReturn(true);
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.isPageEmpty(featurePage));
    assertEquals(MessageError.FEATURES_NOT_FOUND.getStatus(), result.getStatus());
  }

  @Test
  void testExistsByName_throwsFeatureFlagException() {
    String featureName = "Test Feature";
    when(featureRepository.existsByName(featureName)).thenReturn(Boolean.TRUE);
    FeatureFlagException result =
        assertThrows(
            FeatureFlagException.class, () -> featureServiceImpl.existsByName(featureName));
    verify(featureRepository).existsByName(featureName);
    assertEquals(MessageError.FEATURE_ALREADY_EXISTS.getMessage(), result.getMessage());
  }

  @Test
  void testExistsByName() {
    String featureName = "Test Feature";
    when(featureRepository.existsByName(featureName)).thenReturn(Boolean.FALSE);
    boolean exists = featureServiceImpl.existsByName(featureName);
    verify(featureRepository).existsByName(featureName);
    assertFalse(exists);
  }

  @Test
  void testExistsById_throwsFeatureFlagException() {
    UUID featureUUID = mock(UUID.class);
    when(featureRepository.existsById(featureUUID)).thenReturn(Boolean.TRUE);
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.existsById(featureUUID));
    verify(featureRepository).existsById(featureUUID);
    assertEquals(MessageError.FEATURE_ALREADY_EXISTS.getMessage(), result.getMessage());
  }

  @Test
  void testFindById_throwsFeatureFlagException() {
    String featureId = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID featureUUID = UUID.fromString(featureId);
    when(featureRepository.findById(featureUUID)).thenReturn(Optional.empty());
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.findById(featureUUID));
    verify(featureRepository).findById(featureUUID);
    assertEquals(MessageError.FEATURE_NOT_FOUND.getMessage(), result.getMessage());
  }

  @Test
  void testFindById() {
    String featureId = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID featureUUID = UUID.fromString(featureId);
    Feature expectedFeature = mock(Feature.class);
    when(featureRepository.findById(featureUUID)).thenReturn(Optional.of(expectedFeature));
    Feature result = featureServiceImpl.findById(featureUUID);
    verify(featureRepository).findById(featureUUID);
    assertNotNull(result);
  }

  @Test
  void testFindByName_throwsFeatureFlagException() {
    String featureName = "Test Feature";
    when(featureRepository.findByName(featureName)).thenReturn(Optional.empty());
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> featureServiceImpl.findByName(featureName));
    verify(featureRepository).findByName(featureName);
    assertEquals(MessageError.FEATURE_NOT_FOUND.getMessage(), result.getMessage());
  }

  @Test
  void testFindByName() {
    String featureName = "Test Feature";
    Feature expectedFeature = mock(Feature.class);
    when(featureRepository.findByName(featureName)).thenReturn(Optional.of(expectedFeature));
    Feature result = featureServiceImpl.findByName(featureName);
    verify(featureRepository).findByName(featureName);
    assertNotNull(result);
  }

  @Test
  void testCheckFeatureIsActive_returnFalseByEnvironment() {
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
  void testCheckFeatureIsActive_returnFalseByEnabled() {
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
  void testCheckFeatureIsActive_returnTrue() {
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
