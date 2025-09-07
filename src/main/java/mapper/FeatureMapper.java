package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.model.Feature;

@Mapper(componentModel = "spring")
public interface FeatureMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "configs", ignore = true)
    Feature toEntity(FeatureRequestDto dto);

    FeatureResponseDto toDto(Feature entity);
    
}
