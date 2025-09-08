package com.equipo01.featureflag.featureflag.service;

import java.util.List;
import java.util.UUID;

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;

public interface FeatureService {
    
    FeatureResponseDto createFeature(FeatureRequestDto requestDto);

    List<FeatureResponseDto> getAllFeatures();

    FeatureResponseDto getFeatureById(UUID featureId);

}
