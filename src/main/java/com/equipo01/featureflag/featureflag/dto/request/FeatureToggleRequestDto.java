package com.equipo01.featureflag.featureflag.dto.request;

import com.equipo01.featureflag.featureflag.model.enums.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureToggleRequestDto {
  private String clientId; // Nullable
  private Environment environment; // Nullable
}
