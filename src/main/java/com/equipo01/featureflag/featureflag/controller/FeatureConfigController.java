package com.equipo01.featureflag.featureflag.controller;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface FeatureConfigController {
  ResponseEntity<FeatureConfigResponseDto> createFeatureConfig(FeatureConfigRequestDto requestDto);

  ResponseEntity<List<FeatureConfigResponseDto>> getFeatureByID(String id);

  ResponseEntity<List<FeatureConfigResponseDto>> getAllFeatures();

  ResponseEntity<String> checkPermissionTest();

  ResponseEntity<Void> deleteFeatureConfig(@PathVariable String id);
}
