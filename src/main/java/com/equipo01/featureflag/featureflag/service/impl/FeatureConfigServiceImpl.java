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

@Service
@RequiredArgsConstructor
public class FeatureConfigServiceImpl implements FeatureConfigService {
  private final FeatureConfigMapper featureConfigMapper;
  private final FeatureConfigRepository featureConfigRepository;
  private final FeatureService featureService;

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

  public FeatureConfigResponseDto createFeatureConfig(FeatureConfigRequestDto requestDto) {
    // FreatureConfigRequestDto -> FeatureConfig
    FeatureConfig featureConfig = featureConfigMapper.toEntity(requestDto);

    // Le añadimos la relación ya que no puede ser nullo, primero nos aseguramos de que exista, si
    // no existe lanzará error
    UUID featureUUID = UUID.fromString(requestDto.getFeatureId());
    // Si si que existe la añadimos a la entidad
    Feature feature = featureService.findById(featureUUID);
    featureConfig.setFeature(feature);

    // Save featureConfig to the database
    featureConfig = featureConfigRepository.save(featureConfig);

    // FeatureConfig -> FeatureResponseDto
    return featureConfigMapper.toDto(featureConfig);
  }

  public List<FeatureConfigResponseDto> getFeatureByID(UUID id) {
    return featureConfigRepository
        .findById(id)
        .map(featureConfigMapper::toDto)
        .map(List::of)
        .orElseGet(List::of);
  }

  public List<FeatureConfigResponseDto> getAllFeatures() {
    List<FeatureConfig> featureConfigs = featureConfigRepository.findAll();
    List<FeatureConfigResponseDto> responseDtos = featureConfigMapper.toDtoList(featureConfigs);
    return responseDtos;
  }

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
