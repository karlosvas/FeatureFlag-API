package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;

public interface FeatureService {
    
    FeatureResponseDto createFeature(FeatureRequestDto requestDto);

}
