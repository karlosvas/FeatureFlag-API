package com.equipo01.featureflag.featureflag.dto.response;

import java.util.List;

import com.equipo01.featureflag.featureflag.dto.LinksDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFeatureResponseDto {
    
    private List<FeatureResponseDto> features;
    private LinksDto links;
}
