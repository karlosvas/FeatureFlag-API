package com.equipo01.featureflag.featureflag.controller.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equipo01.featureflag.featureflag.controller.FeatureConfigController;
import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.service.FeatureConfigService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("${api.configurations}")
public class FeatureConfigControllerImp implements FeatureConfigController {
    private final FeatureConfigService featureConfigService;
    
    @PostMapping
    public ResponseEntity<FeatureConfigResponseDto> createFeatureConfig(@Valid @RequestBody FeatureConfigRequestDto requestDto){
        return featureConfigService.createFeatureConfig(requestDto);
    };

   @GetMapping("/{id}")
    public ResponseEntity<List<FeatureConfigResponseDto>> getFeatureByID(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        return featureConfigService.getFeatureByID(uuid);
    }

    @GetMapping
    public ResponseEntity<List<FeatureConfigResponseDto>> getAllFeatures() {
        return featureConfigService.getAllFeatures();
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")    
    public ResponseEntity<String> checkPermissionTest() {
        return ResponseEntity.ok("Test permission ok");
    }

    @DeleteMapping("/{featureConfigId}")
    public ResponseEntity<Void> deleteFeatureConfig(@PathVariable String featureConfigId) {
        UUID uuid = UUID.fromString(featureConfigId);
        featureConfigService.deleteFeatureConfig(uuid);
        return ResponseEntity.noContent().build();
    }
}