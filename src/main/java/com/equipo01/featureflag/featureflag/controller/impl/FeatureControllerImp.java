package com.equipo01.featureflag.featureflag.controller.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equipo01.featureflag.featureflag.controller.FeatureController;
import com.equipo01.featureflag.featureflag.dto.request.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.service.FeatureService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.features}")
public class FeatureControllerImp implements FeatureController {

    private final FeatureService featureService;

    @PostMapping
    @SwaggerApiResponses
    @Operation(summary = "Crea una nueva feature flag", description = "Crea una nueva feature flag con los datos proporcionados y devuelve la feature creada.")
    public ResponseEntity<FeatureResponseDto> createFeature(@Valid @RequestBody FeatureRequestDto requestDto) {
        FeatureResponseDto responseDto = featureService.createFeature(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    @SwaggerApiResponses
    @Operation(summary = "Obtiene todas las feature flags", description = "Devuelve una lista de todas las feature flags disponibles.")
    public ResponseEntity<GetFeatureResponseDto> getFeatures(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "enabled", required = false) Boolean enabledByDefault,
            @RequestParam(value = "page", defaultValue = "0", required = false) @Min(value = 0, message = "Page must be at least 0") Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Min(value = 1, message = "Size must be at least 1") Integer size) {

        GetFeatureResponseDto getFeatureResponseDto = featureService.getFeatures(name, enabledByDefault, page, size);
        return ResponseEntity.ok().body(getFeatureResponseDto);
    }

    @GetMapping("/{featureId}")
    @SwaggerApiResponses
    @Operation(summary = "Obtiene una feature flag por su ID", description = "Devuelve los detalles de una feature flag específica identificada por su UUID.")
    public ResponseEntity<FeatureResponseDto> getFeature(
            @PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format") String featureId) {
        return ResponseEntity.ok(featureService.getFeatureById(featureId));
    }

    @GetMapping("/check")
    @SwaggerApiResponses
    @Operation(summary = "Verifica si una feature está activa para un cliente en un entorno específico", description = "Devuelve true si la feature está activa, false en caso contrario.")
    public ResponseEntity<Boolean> checkFeatureIsActive(
            @RequestParam String nameFeature,
            @RequestParam String clientID,
            @RequestParam String environment) {

        Environment env = Environment.valueOf(environment);
        UUID uuid = UUID.fromString(clientID);
        Boolean isActive = featureService.checkFeatureIsActive(nameFeature, uuid, env);
        return ResponseEntity.ok(isActive);
    }

    @Override
    @PutMapping("/{id}/{action:(?:enable|disable)}")
    @SwaggerApiResponses
    @Operation(summary = "Habilita o deshabilita la configuración de una feature para un cliente o entorno específico", description = "Habilita o deshabilita la configuración de una feature para un cliente o entorno específico según el parámetro 'enable'.")
    public ResponseEntity<?> updateFeatureForClientOrEnvironment(
            @PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format") String id,
            @PathVariable String action,
            @RequestBody FeatureToggleRequestDto toggleRequestDto) {

        UUID featureId = UUID.fromString(id);
        boolean enable = action.equalsIgnoreCase("enable");
        featureService.updateFeatureForClientOrEnvironment(featureId, toggleRequestDto, enable);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
