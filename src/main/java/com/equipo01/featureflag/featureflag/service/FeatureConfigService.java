package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface FeatureConfigService {
  public FeatureConfigResponseDto createFeatureConfig(FeatureConfigRequestDto requestDto);

  public List<FeatureConfigResponseDto> getFeatureByID(UUID id);

  public List<FeatureConfigResponseDto> getAllFeatures();

  public ResponseEntity<List<FeatureConfigResponseDto>> enableOrDisableFeature(
      UUID featureConfigId, boolean enable);

  public void deleteFeatureConfig(UUID id);
}
