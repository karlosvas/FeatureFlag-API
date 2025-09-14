package com.equipo01.featureflag.featureflag.dto;

import com.equipo01.featureflag.featureflag.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO que representa a un usuario en el sistema. Incluye detalles como nombre de usuario, roles y
 * estado activo.
 *
 * <p>{@link Getter} Genera los métodos get automáticamente. {@link Setter} Genera los métodos set
 * automáticamente. {@link Builder} Genera el patrón builder para la clase. {@link ToString} Para
 * poder imprimir por consola
 *
 * <p>Atributos - id: Identificador único del usuario. - username: Nombre de usuario para login. -
 * email: Email del usuario. - roles: Roles asignados (USER, ADMIN, VIEWER). - active: Usuario
 * activo/inactivo.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO that represents a user in the system")
public class UserDTO {
  @Schema(
      description = "Identifier unique to the user",
      example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Username for login", example = "usuario01")
  @NotBlank
  @Size(min = 3, max = 50)
  private String username;

  @Schema(description = "Email of the user", example = "usuario01@email.com")
  @Email
  @NotBlank
  private String email;

  @Schema(description = "Role assigned to the user", example = "USER")
  @NotNull
  private Role role;

  @Schema(description = "Indicates if the user is active", example = "true")
  @NotNull
  private Boolean active;
}
