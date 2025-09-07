package com.equipo01.featureflag.featureflag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equipo01.featureflag.featureflag.dto.FeatureRequestDto;
import com.equipo01.featureflag.featureflag.dto.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.service.FeatureService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
@Slf4j
public class FeatureController {
    
    private final FeatureService featureService;

    @PostMapping("path")    
    public ResponseEntity<FeatureResponseDto> createFeature(@Valid @RequestBody FeatureRequestDto requestDto) {
        FeatureResponseDto responseDto = featureService.createFeature(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
