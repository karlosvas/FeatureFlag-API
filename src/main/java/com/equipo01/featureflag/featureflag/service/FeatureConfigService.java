package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import java.util.List;
import java.util.UUID;

public interface FeatureConfigService {
  public FeatureConfigResponseDto createFeatureConfig(
      FeatureConfigRequestDto requestDto);

  public List<FeatureConfigResponseDto> getFeatureByID(UUID id);

  public List<FeatureConfigResponseDto> getAllFeatures();

  public void deleteFeatureConfig(UUID id);
}
