package com.equipo01.featureflag.featureflag.dto.request;

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
public class FeatureConfigRequestDto {
  @NotNull(message = "environment is required")
  private Environment environment;

  @NotBlank(message = "clientId is required")
  private String clientId;

  @NotNull(message = "enabled is required")
  private Boolean enabled;

  @NotBlank(message = "featureId is required")
  private String featureId;
}
