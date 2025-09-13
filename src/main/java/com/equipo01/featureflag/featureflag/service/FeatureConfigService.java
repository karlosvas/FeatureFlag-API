package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface FeatureConfigService {
  public ResponseEntity<FeatureConfigResponseDto> createFeatureConfig(
      FeatureConfigRequestDto requestDto);

  public ResponseEntity<List<FeatureConfigResponseDto>> getFeatureByID(UUID id);

  public ResponseEntity<List<FeatureConfigResponseDto>> getAllFeatures();

  public void deleteFeatureConfig(UUID id);
}
