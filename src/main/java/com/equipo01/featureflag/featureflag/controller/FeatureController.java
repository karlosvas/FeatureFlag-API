package com.equipo01.featureflag.featureflag.controller;

import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interfaz del controlador para gestionar las operaciones relacionadas con las feature flags.
 * Define los endpoints para crear, obtener todas y obtener una feature flag por su ID.
 *
 * <p>Anotaciones de validación: - {@link Valid} para validar el cuerpo de la solicitud al crear una
 * feature flag. - {@link Pattern} para validar el formato del UUID al obtener una feature flag por
 * su
 */
public interface FeatureController {
  /**
   * Crea una nueva feature flag.
   *
   * @param requestDto datos de la feature flag a crear
   * @return la feature flag creada con estado HTTP 201
   */
  public ResponseEntity<FeatureResponseDto> createFeature(
      @Valid @RequestBody FeatureRequestDto requestDto);

  /**
   * Obtiene una lista paginada de todas las feature flags, con filtros opcionales por nombre y
   * estado habilitado.
   *
   * @param name filtro opcional por nombre (coincidencia parcial)
   * @param enabledByDefault filtro opcional por estado habilitado
   * @param page número de página para la paginación (predeterminado: 0)
   * @param size tamaño de página para la paginación (predeterminado: 10)
   * @return una lista paginada de feature flags que coinciden con los filtros aplicados
   */
  public ResponseEntity<GetFeatureResponseDto> getFeatures(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "enabled", required = false) Boolean enabledByDefault,
      @RequestParam(value = "page", defaultValue = "0", required = false)
          @Min(value = 0, message = "Page must be at least 0")
          Integer page,
      @RequestParam(value = "size", defaultValue = "10", required = false)
          @Min(value = 1, message = "Size must be at least 1")
          Integer size);

  /**
   * Retrieves details of a specific feature flag by its UUID.
   *
   * @param featureId the UUID of the feature flag
   * @return the feature flag details
   */
  public ResponseEntity<FeatureResponseDto> getFeature(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format")
          String featureId);

  public ResponseEntity<Boolean> checkFeatureIsActive(
      @RequestParam String nameFeature,
      @RequestParam String clientID,
      @RequestParam String environment);

  public ResponseEntity<Void> deleteFeature(@PathVariable String id);
}
