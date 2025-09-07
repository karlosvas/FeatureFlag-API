package com.equipo01.featureflag.featureflag.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.FeatureAlreadyExistsException;
import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.FeatureMapper;

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
            Feature feature = featureMapper.toEntity(requestDto);
            Feature savedFeature = featureRepository.save(feature);

            log.info("Feature created successfully with id: {}", savedFeature.getId());

            return featureMapper.toDto(savedFeature);
        } catch (DataIntegrityViolationException e) {
            log.error("Feature with name '{}' already exists", requestDto.getName());
            throw new FeatureAlreadyExistsException(requestDto.getName());
        }
    }
    
}
