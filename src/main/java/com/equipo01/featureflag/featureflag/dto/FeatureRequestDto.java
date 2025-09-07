package com.equipo01.featureflag.featureflag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureRequestDto {
    
    @NotBlank(message = "Feature name is required")
    private String name;

    @NotBlank(message = "Feature description is required")
    private String description;

    @NotNull(message = "Feature enabledByDefault status is required")
    @Builder.Default
    private Boolean enabledByDefault = true;

}
