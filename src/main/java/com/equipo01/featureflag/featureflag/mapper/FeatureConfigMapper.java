package com.equipo01.featureflag.featureflag.mapper;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper interface for converting between FeatureConfig entities and DTOs.
 *
 * <p>This interface provides mapping functionality between FeatureConfig domain entities and their
 * corresponding Data Transfer Objects (DTOs) for both request and response scenarios. It handles
 * the conversion logic needed to maintain separation between the data layer and the
 * presentation/API layer for feature configuration operations.
 *
 * <p>The mapper automatically generates implementation code at compile time using MapStruct
 * annotations, ensuring type-safe and performant conversions. It uses the shared
 * MapperConfiguration for consistent behavior across all mappers.
 *
 * <p>Key mapping behaviors: - Request DTO to entity conversion with feature relationship management
 * - Entity to response DTO conversion with feature ID extraction - Collection mappings for bulk
 * operations - Proper handling of nested feature relationships
 *
 * <p>Special mapping considerations: - Feature relationship is ignored during entity creation
 * (managed separately) - Feature ID is extracted from the nested feature entity for response DTOs -
 * Maintains data integrity through proper relationship handling
 */
@Mapper(config = MapperConfiguration.class)
public interface FeatureConfigMapper {

  /**
   * Converts a FeatureConfigRequestDto to a FeatureConfig entity for persistence operations.
   *
   * @param dto The feature configuration request DTO containing configuration data
   * @return FeatureConfig entity ready for feature relationship assignment and persistence
   * @throws IllegalArgumentException if the dto parameter is null
   */
  @Mapping(target = "feature", ignore = true)
  FeatureConfig toEntity(FeatureConfigRequestDto dto);

  /**
   * Converts a FeatureConfig entity to a FeatureConfigResponseDto for API responses.
   *
   * @param entity The feature configuration entity from the persistence layer
   * @return FeatureConfigResponseDto containing configuration data with feature ID
   * @throws IllegalArgumentException if the entity parameter is null
   */
  @Mapping(target = "featureId", source = "feature.id")
  FeatureConfigResponseDto toDto(FeatureConfig entity);

  /**
   * Converts a list of FeatureConfig entities to a list of FeatureConfigResponseDtos.
   *
   * @param entities List of feature configuration entities from the persistence layer
   * @return List of FeatureConfigResponseDto objects for API response
   * @throws IllegalArgumentException if the entities parameter is null
   */
  @Mapping(target = "featureId", source = "feature.id")
  public List<FeatureConfigResponseDto> toDtoList(List<FeatureConfig> entities);
}
