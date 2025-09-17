package com.equipo01.featureflag.featureflag.mapper;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.model.Feature;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper interface for converting between Feature entities and DTOs.
 *
 * <p>This interface provides bi-directional mapping functionality between Feature domain entities
 * and their corresponding Data Transfer Objects (DTOs). It handles the conversion logic needed to
 * maintain separation between the data layer and the presentation/API layer.
 *
 * <p>The mapper automatically generates implementation code at compile time using MapStruct
 * annotations, ensuring type-safe and performant conversions. It uses the shared
 * MapperConfiguration for consistent behavior across all mappers.
 *
 * <p>Key mapping behaviors: - Entity creation ignores auto-generated fields (id, configs) -
 * Explicit mapping for enabledByDefault field preservation - Support for both single entity and
 * collection conversions
 *
 * <p>Annotations used: - @Mapper: Indicates that this interface is a MapStruct mapper - @Mapping:
 * Defines individual field mappings between source and target types
 */
@Mapper(config = MapperConfiguration.class)
public interface FeatureMapper {

  /**
   * Converts a FeatureRequestDto to a Feature entity for persistence operations.
   *
   * @param dto The feature request DTO containing data from the API layer
   * @return Feature entity ready for persistence, with auto-generated fields excluded
   * @throws IllegalArgumentException if the dto parameter is null
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "configs", ignore = true)
  @Mapping(target = "enabledByDefault", source = "enabledByDefault")
  Feature toEntity(FeatureRequestDto dto);

  /**
   * Converts a Feature entity to a FeatureResponseDto for API responses.
   *
   * @param entity The feature entity from the persistence layer
   * @return FeatureResponseDto containing all feature data for API response
   * @throws IllegalArgumentException if the entity parameter is null
   */
  FeatureResponseDto toDto(Feature entity);

  /**
   * Converts a list of Feature entities to a list of FeatureResponseDtos.
   *
   * @param entities List of feature entities from the persistence layer
   * @return List of FeatureResponseDto objects for API response
   * @throws IllegalArgumentException if the entities parameter is null
   */
  List<FeatureResponseDto> toDtoList(List<Feature> entities);
}
