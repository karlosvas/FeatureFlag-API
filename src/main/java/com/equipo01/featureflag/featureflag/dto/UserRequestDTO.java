package com.equipo01.featureflag.featureflag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * DTO representing the request to create a new user.
 * Includes the data necessary to create a user in the system.
 *
 * Annotations used:
 * - {@link Getter} Automatically generates get methods.
 *
 * Attributes
 * - username: Username for login.
 * - email: User's email address.
 * - password: User's password.
 */
@Getter
@Schema(description = "DTO representing the request to create a new user.")
public class UserRequestDTO {
    @Schema(description = "Username for login", example = "user01")
    @NotBlank(message =  "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(description = "User's email address", example = "user01@email.com")
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "User password", example = "securePassword123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
