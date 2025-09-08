package com.equipo01.featureflag.featureflag.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureResponseDto {
    
    private UUID id;
    private String name;
    private String description;
    private boolean enabledByDefault;
}
