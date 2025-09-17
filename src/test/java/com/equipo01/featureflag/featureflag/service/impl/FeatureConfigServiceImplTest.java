package com.equipo01.featureflag.featureflag.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.FeatureConfigMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.repository.FeatureConfigRepository;
import com.equipo01.featureflag.featureflag.service.FeatureService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class FeatureConfigServiceImplTest {

  @Mock private FeatureConfigMapper featureConfigMapper;

  @Mock private FeatureConfigRepository featureConfigRepository;

  @Mock private FeatureService featureService;

  @InjectMocks private FeatureConfigServiceImpl featureConfigService;

  private FeatureConfig featureConfig;
  private FeatureConfigRequestDto requestDto;
  private FeatureConfigResponseDto responseDto;
  private Feature feature;
  private UUID featureConfigId;
  private UUID featureId;

  @BeforeEach
  void setUp() {
    featureConfigId = UUID.randomUUID();
    featureId = UUID.randomUUID();

    // Create test feature
    feature = Feature.builder()
        .id(featureId)
        .name("test-feature")
        .description("Test feature description")
        .enabledByDefault(false)
        .build();

    // Create test feature config
    featureConfig = FeatureConfig.builder()
        .id(featureConfigId)
        .feature(feature)
        .environment(Environment.DEV)
        .clientId("test-client")
        .enabled(true)
        .build();

    // Create test request DTO
    requestDto = FeatureConfigRequestDto.builder()
        .featureId(featureId.toString())
        .environment(Environment.DEV)
        .clientId("test-client")
        .enabled(true)
        .build();

    // Create test response DTO
    responseDto = FeatureConfigResponseDto.builder()
        .id(featureConfigId)
        .featureId(featureId)
        .environment(Environment.DEV)
        .clientId("test-client")
        .enabled(true)
        .build();
  }

  @Test
  void testEnableOrDisableFeature_Success_EnableFeature() {
    // Given
    when(featureConfigRepository.findById(featureConfigId)).thenReturn(Optional.of(featureConfig));
    when(featureConfigRepository.save(any(FeatureConfig.class))).thenReturn(featureConfig);
    when(featureConfigMapper.toDto(featureConfig)).thenReturn(responseDto);

    // When
    ResponseEntity<List<FeatureConfigResponseDto>> result = 
        featureConfigService.enableOrDisableFeature(featureConfigId, true);

    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    List<FeatureConfigResponseDto> body = result.getBody();
    assertNotNull(body);
    assertFalse(body.isEmpty());
    assertEquals(1, body.size());
    assertEquals(responseDto, body.get(0));
    
    verify(featureConfigRepository).findById(featureConfigId);
    verify(featureConfigRepository).save(featureConfig);
    verify(featureConfigMapper).toDto(featureConfig);
    assertTrue(featureConfig.getEnabled());
  }

  @Test
  void testEnableOrDisableFeature_Success_DisableFeature() {
    // Given
    when(featureConfigRepository.findById(featureConfigId)).thenReturn(Optional.of(featureConfig));
    when(featureConfigRepository.save(any(FeatureConfig.class))).thenReturn(featureConfig);
    when(featureConfigMapper.toDto(featureConfig)).thenReturn(responseDto);

    // When
    ResponseEntity<List<FeatureConfigResponseDto>> result = 
        featureConfigService.enableOrDisableFeature(featureConfigId, false);

    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    List<FeatureConfigResponseDto> body = result.getBody();
    assertNotNull(body);
    assertEquals(1, body.size());
    
    verify(featureConfigRepository).findById(featureConfigId);
    verify(featureConfigRepository).save(featureConfig);
    verify(featureConfigMapper).toDto(featureConfig);
    assertFalse(featureConfig.getEnabled());
  }

  @Test
  void testEnableOrDisableFeature_NotFound() {
    // Given
    when(featureConfigRepository.findById(featureConfigId)).thenReturn(Optional.empty());

    // When
    ResponseEntity<List<FeatureConfigResponseDto>> result = 
        featureConfigService.enableOrDisableFeature(featureConfigId, true);

    // Then
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    assertNull(result.getBody());
    
    verify(featureConfigRepository).findById(featureConfigId);
    verify(featureConfigRepository, never()).save(any());
    verify(featureConfigMapper, never()).toDto(any());
  }

  @Test
  void testCreateFeatureConfig_Success() {
    // Given
    when(featureConfigMapper.toEntity(requestDto)).thenReturn(featureConfig);
    when(featureService.findById(featureId)).thenReturn(feature);
    when(featureConfigRepository.save(any(FeatureConfig.class))).thenReturn(featureConfig);
    when(featureConfigMapper.toDto(featureConfig)).thenReturn(responseDto);

    // When
    FeatureConfigResponseDto result = featureConfigService.createFeatureConfig(requestDto);

    // Then
    assertNotNull(result);
    assertEquals(responseDto, result);
    
    verify(featureConfigMapper).toEntity(requestDto);
    verify(featureService).findById(featureId);
    verify(featureConfigRepository).save(featureConfig);
    verify(featureConfigMapper).toDto(featureConfig);
    assertEquals(feature, featureConfig.getFeature());
  }

  @Test
  void testCreateFeatureConfig_InvalidFeatureId() {
    // Given
    requestDto.setFeatureId("invalid-uuid");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> 
        featureConfigService.createFeatureConfig(requestDto));
    
    verify(featureConfigMapper).toEntity(requestDto);
    verify(featureService, never()).findById(any());
    verify(featureConfigRepository, never()).save(any());
  }

  @Test
  void testCreateFeatureConfig_FeatureNotFound() {
    // Given
    when(featureConfigMapper.toEntity(requestDto)).thenReturn(featureConfig);
    when(featureService.findById(featureId)).thenThrow(new FeatureFlagException(
        MessageError.FEATURE_NOT_FOUND.getStatus(),
        MessageError.FEATURE_NOT_FOUND.getMessage(),
        MessageError.FEATURE_NOT_FOUND.getDescription()));

    // When & Then
    assertThrows(FeatureFlagException.class, () -> 
        featureConfigService.createFeatureConfig(requestDto));
    
    verify(featureConfigMapper).toEntity(requestDto);
    verify(featureService).findById(featureId);
    verify(featureConfigRepository, never()).save(any());
  }

  @Test
  void testGetFeatureByID_Success() {
    // Given
    when(featureConfigRepository.findById(featureConfigId)).thenReturn(Optional.of(featureConfig));
    when(featureConfigMapper.toDto(featureConfig)).thenReturn(responseDto);

    // When
    List<FeatureConfigResponseDto> result = featureConfigService.getFeatureByID(featureConfigId);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(responseDto, result.get(0));
    
    verify(featureConfigRepository).findById(featureConfigId);
    verify(featureConfigMapper).toDto(featureConfig);
  }

  @Test
  void testGetFeatureByID_NotFound() {
    // Given
    when(featureConfigRepository.findById(featureConfigId)).thenReturn(Optional.empty());

    // When
    List<FeatureConfigResponseDto> result = featureConfigService.getFeatureByID(featureConfigId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    
    verify(featureConfigRepository).findById(featureConfigId);
    verify(featureConfigMapper, never()).toDto(any());
  }

  @Test
  void testGetAllFeatures_Success() {
    // Given
    FeatureConfig featureConfig2 = FeatureConfig.builder()
        .id(UUID.randomUUID())
        .feature(feature)
        .environment(Environment.PROD)
        .clientId("test-client-2")
        .enabled(false)
        .build();

    FeatureConfigResponseDto responseDto2 = FeatureConfigResponseDto.builder()
        .id(featureConfig2.getId())
        .featureId(featureId)
        .environment(Environment.PROD)
        .clientId("test-client-2")
        .enabled(false)
        .build();

    List<FeatureConfig> featureConfigs = Arrays.asList(featureConfig, featureConfig2);
    List<FeatureConfigResponseDto> responseDtos = Arrays.asList(responseDto, responseDto2);

    when(featureConfigRepository.findAll()).thenReturn(featureConfigs);
    when(featureConfigMapper.toDtoList(featureConfigs)).thenReturn(responseDtos);

    // When
    List<FeatureConfigResponseDto> result = featureConfigService.getAllFeatures();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(responseDtos, result);
    
    verify(featureConfigRepository).findAll();
    verify(featureConfigMapper).toDtoList(featureConfigs);
  }

  @Test
  void testGetAllFeatures_EmptyList() {
    // Given
    when(featureConfigRepository.findAll()).thenReturn(Arrays.asList());
    when(featureConfigMapper.toDtoList(Arrays.asList())).thenReturn(Arrays.asList());

    // When
    List<FeatureConfigResponseDto> result = featureConfigService.getAllFeatures();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    
    verify(featureConfigRepository).findAll();
    verify(featureConfigMapper).toDtoList(Arrays.asList());
  }

  @Test
  void testDeleteFeatureConfig_Success() {
    // Given
    when(featureConfigRepository.existsById(featureConfigId)).thenReturn(true);

    // When
    assertDoesNotThrow(() -> featureConfigService.deleteFeatureConfig(featureConfigId));

    // Then
    verify(featureConfigRepository).existsById(featureConfigId);
    verify(featureConfigRepository).deleteById(featureConfigId);
  }

  @Test
  void testDeleteFeatureConfig_NotFound() {
    // Given
    when(featureConfigRepository.existsById(featureConfigId)).thenReturn(false);

    // When & Then
    FeatureFlagException exception = assertThrows(FeatureFlagException.class, () -> 
        featureConfigService.deleteFeatureConfig(featureConfigId));
    
    assertEquals(MessageError.FEATURE_CONFIG_NOT_FOUND.getStatus(), exception.getStatus());
    assertEquals(MessageError.FEATURE_CONFIG_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(MessageError.FEATURE_CONFIG_NOT_FOUND.getDescription(), exception.getDescription());
    
    verify(featureConfigRepository).existsById(featureConfigId);
    verify(featureConfigRepository, never()).deleteById(any());
  }

  @Test
  void testCreateFeatureConfig_MapperIntegration() {
    // Given
    FeatureConfig mappedEntity = FeatureConfig.builder()
        .environment(Environment.DEV)
        .clientId("test-client")
        .enabled(true)
        .build();

    when(featureConfigMapper.toEntity(requestDto)).thenReturn(mappedEntity);
    when(featureService.findById(featureId)).thenReturn(feature);
    when(featureConfigRepository.save(any(FeatureConfig.class))).thenReturn(featureConfig);
    when(featureConfigMapper.toDto(featureConfig)).thenReturn(responseDto);

    // When
    FeatureConfigResponseDto result = featureConfigService.createFeatureConfig(requestDto);

    // Then
    assertNotNull(result);
    assertEquals(responseDto.getId(), result.getId());
    assertEquals(responseDto.getEnvironment(), result.getEnvironment());
    assertEquals(responseDto.getClientId(), result.getClientId());
    assertEquals(responseDto.getEnabled(), result.getEnabled());
    
    // Verify that the feature was set correctly
    verify(featureConfigRepository).save(argThat(config -> 
        config.getFeature().equals(feature) &&
        config.getEnvironment().equals(Environment.DEV) &&
        config.getClientId().equals("test-client") &&
        config.getEnabled()
    ));
  }

  @Test
  void testEnableOrDisableFeature_StateTransitions() {
    // Test enabling an already enabled feature
    featureConfig.setEnabled(true);
    
    when(featureConfigRepository.findById(featureConfigId)).thenReturn(Optional.of(featureConfig));
    when(featureConfigRepository.save(any(FeatureConfig.class))).thenReturn(featureConfig);
    when(featureConfigMapper.toDto(featureConfig)).thenReturn(responseDto);

    // Enable already enabled feature
    ResponseEntity<List<FeatureConfigResponseDto>> result1 = 
        featureConfigService.enableOrDisableFeature(featureConfigId, true);
    
    assertEquals(HttpStatus.OK, result1.getStatusCode());
    assertTrue(featureConfig.getEnabled());

    // Disable the feature
    ResponseEntity<List<FeatureConfigResponseDto>> result2 = 
        featureConfigService.enableOrDisableFeature(featureConfigId, false);
    
    assertEquals(HttpStatus.OK, result2.getStatusCode());
    assertFalse(featureConfig.getEnabled());

    verify(featureConfigRepository, times(2)).findById(featureConfigId);
    verify(featureConfigRepository, times(2)).save(featureConfig);
  }
}