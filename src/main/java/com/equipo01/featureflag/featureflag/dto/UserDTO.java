package com.equipo01.featureflag.featureflag.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import com.equipo01.featureflag.featureflag.model.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa a un usuario en el sistema.
 * Incluye detalles como nombre de usuario, roles y estado activo.
 * 
 * {@link Getter} Genera los métodos get automáticamente.
 * {@link Setter} Genera los métodos set automáticamente.
 * {@link Builder} Genera el patrón builder para la clase.
 * {@link ToString} Para poder imprimir por consola
 * 
 * Atributos
 * - id: Identificador único del usuario.
 * - username: Nombre de usuario para login.
 * - email: Email del usuario.
 * - roles: Roles asignados (USER, ADMIN, VIEWER).
 * - active: Usuario activo/inactivo.
 */
@Getter
@Setter
@Builder
@ToString
@Schema(description = "DTO que representa a un usuario en el sistema.")
public class UserDTO {
	@Schema(description = "Identificador único del usuario", example = "123e4567-e89b-12d3-a456-426614174000")
	private UUID id;

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
	@Size(min = 8, max = 100)
	private String password;

	@Schema(description = "Rol asignado al usuario", example = "USER")
	@NotNull
	private Role role;

	@Schema(description = "Indica si el usuario está activo", example = "true")
	@NotNull
	private Boolean active;
}