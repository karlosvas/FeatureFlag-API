package com.equipo01.featureflag.featureflag.controller.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.controller.FeatureController;
import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * Implementación del controlador para gestionar las feature flags.
 * Proporciona endpoints para crear, obtener todas y obtener una feature flag por su ID.
 * Utiliza FeatureService para la lógica de negocio.
 *
 * Anotación
 * - {@link RestController}: Indica que esta clase es un controlador REST.
 * - {@link RequiredArgsConstructor}: Genera un constructor con los campos finales.
 * - {@link RequestMapping}: Define la ruta base para los endpoints del controlador.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.features}")
public class FeatureControllerImp implements FeatureController {

    private final FeatureService featureService;

     /**
     * Crea una nueva feature flag.
     *
     * @param requestDto datos de la feature flag a crear
     * @return la feature flag creada con estado HTTP 201
     */
    @PostMapping
    @SwaggerApiResponses
    @Operation(summary = "Crea una nueva feature flag", description = "Crea una nueva feature flag con los datos proporcionados y devuelve la feature creada.")
    public ResponseEntity<FeatureResponseDto> createFeature(@Valid @RequestBody FeatureRequestDto requestDto) {
        FeatureResponseDto responseDto = featureService.createFeature(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Obtiene una lista de todas las feature flags.
     *
     * @return una lista de feature flags
     */
    @GetMapping
    @SwaggerApiResponses
    @Operation(summary = "Obtiene todas las feature flags", description = "Devuelve una lista de todas las feature flags disponibles.")
    public ResponseEntity<List<FeatureResponseDto>> getFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    /**
     * Obtiene los detalles de una feature flag específica identificada por su UUID.
     *
     * @param featureId UUID de la feature flag
     * @return detalles de la feature flag
     */
    @GetMapping("/{featureId}")
    @SwaggerApiResponses
    @Operation(summary = "Obtiene una feature flag por su ID", description = "Devuelve los detalles de una feature flag específica identificada por su UUID.")
    public ResponseEntity<FeatureResponseDto> getFeature(@PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format") String featureId) {
            UUID uuid = UUID.fromString(featureId);
        return ResponseEntity.ok(featureService.getFeatureById(uuid));
    }

}
