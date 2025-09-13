package com.equipo01.featureflag.featureflag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "DTO for user login request.")
public class LoginRequestDto {
    @Schema(description = "Username for login", example = "user01")
    @NotBlank(message =  "Username is required")
    private String username;

    @Schema(description = "User password", example = "securePassword123")
    @NotBlank(message = "Password is required")
    private String password;
}
