package com.equipo01.featureflag.featureflag.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
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
import com.equipo01.featureflag.featureflag.service.FeatureService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;
    private final FeatureMapper featureMapper;
    private final Logger logger = Logger.getLogger(FeatureServiceImpl.class.getName());

    @Override
    @Transactional
    public FeatureResponseDto createFeature(FeatureRequestDto requestDto) {
        logger.info("Feature request DTO: " + requestDto);
        try {

            if (featureRepository.existsByName(requestDto.getName())) {
                logger.warning("Feature with name '" + requestDto.getName() + "' already exists");
                throw new FeatureAlreadyExistsException(requestDto.getName());
            }

            Feature feature = featureMapper.toEntity(requestDto);
            logger.info("Feature entity created: " + feature);
            Feature savedFeature = featureRepository.save(feature);

            logger.info("Feature created successfully with id: " + savedFeature.getId());
            return featureMapper.toDto(savedFeature);
        } catch (DataIntegrityViolationException e) {
            logger.severe("Feature with name '" + requestDto.getName() + "' already exists");
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
