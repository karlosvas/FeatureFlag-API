package com.equipo01.featureflag.featureflag.controller;

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
import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.service.FeatureService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.features}")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    /**
     * Creates a new feature flag.
     *
     * @param requestDto the feature flag data to create
     * @return the created feature flag with HTTP 201 status
     */
    @PostMapping
    @SwaggerApiResponses
    @Operation(summary = "Creates new feature flag", description = "Creates a new feature flag with the provided data and returns the created feature.")
    public ResponseEntity<FeatureResponseDto> createFeature(@Valid @RequestBody FeatureRequestDto requestDto) {
        FeatureResponseDto responseDto = featureService.createFeature(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Retrieves a list of all feature flags.
     *
     * @return a list of feature flags
     */
    @GetMapping
    @SwaggerApiResponses
    @Operation(summary = "Retrieves all feature flags", description = "Returns a list of all available feature flags.")
    public ResponseEntity<List<FeatureResponseDto>> getFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    /**
     * Retrieves details of a specific feature flag by its UUID.
     *
     * @param featureId the UUID of the feature flag
     * @return the feature flag details
     */
    @GetMapping("/{featureId}")
    @SwaggerApiResponses
    @Operation(summary = "Retrieves a feature flag by its ID", description = "Returns the details of a specific feature flag identified by its UUID.")
    public ResponseEntity<FeatureResponseDto> getFeature(@PathVariable @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Invalid UUID format") String featureId) {
            UUID uuid = UUID.fromString(featureId);
        return ResponseEntity.ok(featureService.getFeatureById(uuid));
    }

}
