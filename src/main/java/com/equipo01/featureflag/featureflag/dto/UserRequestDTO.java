package com.equipo01.featureflag.featureflag.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO que representa la solicitud de creación de un nuevo usuario.
 * Incluye los datos necesarios para crear un usuario en el sistema.
 *
 * Anotaciones utilizadas:
 * - {@link Data} Genera automáticamente los métodos getter, setter, toString, equals y hashCode.
 *
 * Atributos
 * - username: Nombre de usuario para login.
 * - email: Email del usuario.
 * - password: Contraseña del usuario.
 */
@Data
public class UserRequestDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
