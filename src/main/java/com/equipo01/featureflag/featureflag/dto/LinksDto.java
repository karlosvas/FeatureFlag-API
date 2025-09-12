package com.equipo01.featureflag.featureflag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinksDto {
    
    @JsonProperty("count")
    private String count;

    @JsonProperty("first")
    private LinkDto first;

    @JsonProperty("last")
    private LinkDto last;

    @JsonProperty("next")
    private LinkDto next;

    @JsonProperty("prev")
    private LinkDto prev;
}
