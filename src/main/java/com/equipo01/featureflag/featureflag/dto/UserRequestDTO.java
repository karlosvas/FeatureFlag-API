package com.equipo01.featureflag.featureflag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

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
@Schema(description = "DTO for user creation request")
public class UserRequestDTO {
    @Schema(description = "Username for login", example = "usuario01")
    @NotBlank(message =  "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(description = "Email of the user", example = "usuario01@email.com")
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "User password", example = "passwordSeguro123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
