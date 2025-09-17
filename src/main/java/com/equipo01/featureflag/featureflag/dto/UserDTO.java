package com.equipo01.featureflag.featureflag.dto;

import com.equipo01.featureflag.featureflag.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO representing a user in the system. Includes details such as username, roles, and active
 * status.
 *
 * <p>{@link Getter} Automatically generates get methods. {@link Setter} Automatically generates set
 * methods. {@link Builder} Generates the builder pattern for the class. {@link ToString} For
 * console printing.
 *
 * <p>Attributes - id: Unique user identifier. - username: Username for login. - email: User's email
 * address. - roles: Assigned roles (USER, ADMIN, VIEWER). - active: Active/inactive user.
 */
@Getter
@Setter
@Builder
@ToString
@Schema(description = "DTO representing a user in the system.")
public class UserDTO {
  @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Username for login", example = "user01")
  @NotBlank
  @Size(min = 3, max = 50)
  private String username;

  @Schema(description = "User's email address", example = "user01@email.com")
  @Email
  @NotBlank
  private String email;

  @Schema(description = "User password", example = "securePassword123")
  @NotBlank
  @Size(min = 8, max = 100)
  private String password;

  @Schema(description = "Role assigned to the user", example = "USER")
  @NotNull
  private Role role;

  @Schema(description = "Indicates whether the user is active", example = "true")
  @NotNull
  private Boolean active;
}
