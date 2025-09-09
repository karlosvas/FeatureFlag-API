package com.equipo01.featureflag.featureflag.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

/**
 * Interfaz del controlador para gestionar las operaciones relacionadas con las feature flags.
 * Define los endpoints para crear, obtener todas y obtener una feature flag por su ID.
 *
 * Anotaciones de validación:
 * - {@link Valid} para validar el cuerpo de la solicitud al crear una feature flag.
 * - {@link Pattern} para validar el formato del UUID al obtener una feature flag por su
 */
public interface FeatureController {
    /**
     * Crea una nueva feature flag.
     *
     * @param requestDto datos de la feature flag a crear
     * @return la feature flag creada con estado HTTP 201
     */
    public ResponseEntity<FeatureResponseDto> createFeature(@Valid @RequestBody FeatureRequestDto requestDto);

    /**
     * Obtiene una lista de todas las feature flags.
     *
     * @return una lista de feature flags
     */
    public ResponseEntity<List<FeatureResponseDto>> getFeatures();

    /**
     * Obtiene los detalles de una feature flag específica identificada por su UUID.
     *
     * @param featureId UUID de la feature flag
     * @return detalles de la feature flag
     */
    public ResponseEntity<FeatureResponseDto> getFeature(@PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Formato de UUID inválido") String featureId);
}