package com.equipo01.featureflag.featureflag.mapper;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface FeatureConfigMapper {
  @Mapping(target = "feature", ignore = true)
  FeatureConfig toEntity(FeatureConfigRequestDto dto);

  @Mapping(target = "featureId", source = "feature.id")
  FeatureConfigResponseDto toDto(FeatureConfig entity);

  @Mapping(target = "featureId", source = "feature.id")
  public List<FeatureConfigResponseDto> toDtoList(List<FeatureConfig> entities);
}
