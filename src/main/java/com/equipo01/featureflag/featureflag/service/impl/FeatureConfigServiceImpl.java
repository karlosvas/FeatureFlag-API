package com.equipo01.featureflag.featureflag.service.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.mapper.FeatureConfigMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.repository.FeatureConfigRepository;
import com.equipo01.featureflag.featureflag.service.FeatureConfigService;
import com.equipo01.featureflag.featureflag.service.FeatureService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureConfigServiceImpl implements FeatureConfigService {
    private final FeatureConfigMapper featureConfigMapper;
    private final FeatureConfigRepository featureConfigRepository;
    private final FeatureService featureService;

    public ResponseEntity<FeatureConfigResponseDto> createFeatureConfig(FeatureConfigRequestDto requestDto){
        // FreatureConfigRequestDto -> FeatureConfig
        FeatureConfig featureConfig = featureConfigMapper.toEntity(requestDto);

        // Le añadimos la relación ya que no puede ser nullo, primero nos aseguramos de que exista, si no existe lanzará error
        UUID featureUUID = UUID.fromString(requestDto.getFeatureId());
        // Si si que existe la añadimos a la entidad
        Feature feature = featureService.findById(featureUUID);
        featureConfig.setFeature(feature);

        // Save featureConfig to the database
        featureConfig = featureConfigRepository.save(featureConfig);

        // FeatureConfig -> FeatureResponseDto
        return ResponseEntity.ok(featureConfigMapper.toDto(featureConfig));
    };
     
    public ResponseEntity<List<FeatureConfigResponseDto>> getFeatureByID(UUID id) {
        return ResponseEntity.ok(featureConfigRepository.findById(id)
                .map(featureConfigMapper::toDto)
                .map(List::of)
                .orElseGet(List::of));
    }

    public ResponseEntity<List<FeatureConfigResponseDto>> getAllFeatures() {
        List<FeatureConfig> featureConfigs = featureConfigRepository.findAll();
        List<FeatureConfigResponseDto> responseDtos = featureConfigMapper.toDtoList(featureConfigs);
        return ResponseEntity.ok(responseDtos);
    }
}
