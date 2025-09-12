package com.equipo01.featureflag.featureflag.dto.response;

import java.util.UUID;
import com.equipo01.featureflag.featureflag.model.enums.Environment;

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
public class FeatureConfigResponseDto {
    @NotNull(message = "id is required")
    private UUID id;

    @NotBlank(message = "environment is required")
    private Environment environment;

    @NotBlank(message = "clientId is required")
    private String clientId;

    @NotNull(message = "enabled is required")
    private Boolean enabled;

    @NotNull(message = "featureId is required")
    private UUID featureId;
}
