package com.equipo01.featureflag.featureflag.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureAlreadyExistsException;
import com.equipo01.featureflag.featureflag.exception.FeatureNotFoundException;
import com.equipo01.featureflag.featureflag.mapper.FeatureMapper;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;
    private final FeatureMapper featureMapper;

    @Override
    @Transactional
    public FeatureResponseDto createFeature(FeatureRequestDto requestDto) {
        log.info("Creating feature with name: {}", requestDto.getName());
        try {

            if (featureRepository.existsByName(requestDto.getName())) {
                log.warn("Feature with name '{}' already exists", requestDto.getName());
                throw new FeatureAlreadyExistsException(requestDto.getName());
            }

            Feature feature = featureMapper.toEntity(requestDto);
            Feature savedFeature = featureRepository.save(feature);

            log.info("Feature created successfully with id: {}", savedFeature.getId());

            return featureMapper.toDto(savedFeature);
        } catch (DataIntegrityViolationException e) {
            log.error("Feature with name '{}' already exists", requestDto.getName());
            throw new FeatureAlreadyExistsException(requestDto.getName());
        }
    }

    @Transactional(readOnly = true)
    public List<FeatureResponseDto> getAllFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(featureMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public FeatureResponseDto getFeatureById(UUID featureId) {
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new FeatureNotFoundException(featureId));
        return featureMapper.toDto(feature);
    }
    
}
