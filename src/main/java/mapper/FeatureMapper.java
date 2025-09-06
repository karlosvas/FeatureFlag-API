package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeatureMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "configs", ignore = true)
    Feature toEntity(FeatureRequestDto dto);

    FeatureResponseDto toDto(Feature entity);
    
}
