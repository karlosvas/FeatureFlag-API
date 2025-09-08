package com.equipo01.featureflag.featureflag.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa la solicitud de creación de un nuevo usuario.
 * Incluye los datos necesarios para crear un usuario en el sistema.
 *
 * Anotaciones utilizadas:
 * - {@link Getter} Genera los métodos get automáticamente.
 *
 * Atributos
 * - username: Nombre de usuario para login.
 * - email: Email del usuario.
 * - password: Contraseña del usuario.
 */
@Getter
@Schema(description = "DTO para la solicitud de creación de un nuevo usuario.")
public class UserRequestDTO {
    @Schema(description = "Nombre de usuario para login", example = "usuario01")
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Schema(description = "Email del usuario", example = "usuario01@email.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Contraseña del usuario", example = "passwordSeguro123")
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
