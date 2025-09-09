package com.equipo01.featureflag.featureflag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "DTO para la solicitud de login de un usuario.")
public class LoginRequestDto {
    @Schema(description = "Nombre de usuario para login", example = "usuario01")
    @NotBlank(message =  "Username is required")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "passwordSeguro123")
    @NotBlank(message = "Password is required")
    private String password;
}
