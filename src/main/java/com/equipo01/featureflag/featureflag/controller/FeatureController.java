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

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.service.FeatureService;

import jakarta.validation.Valid;
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
    public ResponseEntity<FeatureResponseDto> getFeature(@PathVariable UUID featureId) {
        return ResponseEntity.ok(featureService.getFeatureById(featureId));
    }

}
