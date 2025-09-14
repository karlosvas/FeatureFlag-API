package com.equipo01.featureflag.featureflag.mapper;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.model.Feature;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface FeatureMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "configs", ignore = true)
  Feature toEntity(FeatureRequestDto dto);

  FeatureResponseDto toDto(Feature entity);

  List<FeatureResponseDto> toDtoList(List<Feature> entities);
}
