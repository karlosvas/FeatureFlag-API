package com.equipo01.featureflag.featureflag.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Global configuration interface for MapStruct mappers in the Feature Flag
 * application.
 * 
 * This interface defines common configuration settings that are shared across
 * all
 * MapStruct mappers in the application. It centralizes mapper behavior to
 * ensure
 * consistency and reduce configuration duplication throughout the codebase.
 * 
 * Configuration includes:
 * - Spring component model integration for dependency injection
 * - Unmapped target policy to handle missing field mappings gracefully
 * 
 * All mappers that use this configuration will automatically:
 * - Be registered as Spring beans for dependency injection
 * - Ignore unmapped target fields without throwing compilation errors
 * - Follow consistent mapping behavior across the application
 * 
 * Annotations used:
 * - {@link MapperConfig} MapStruct annotation indicating that this interface is
 * a mapper configuration.
 * - {@link ReportingPolicy} Enum defining policies for handling unmapped
 * fields.
 */
@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapperConfiguration {

}
