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
public class UserDTO {
	private UUID id;

	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 8, max = 100)
	private String password;

	@NotNull
	private Role role;

	@NotNull
	private Boolean active;
}