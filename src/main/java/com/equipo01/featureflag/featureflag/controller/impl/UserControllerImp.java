package com.equipo01.featureflag.featureflag.controller.impl;

import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para gestionar las operaciones relacionadas con los usuarios. Utiliza el servicio
 * UserService para realizar las operaciones de negocio.
 *
 * <p>Proporciona endpoints para crear, actualizar y eliminar usuarios. {@link RestController}
 * Anotación de Spring que indica que esta clase es un controlador REST. {@link RequestMapping}
 * Anotación de Spring que define la ruta base para todos los endpoints en este controlador.
 */
@RestController
@RequestMapping("${api.auth}")
public class UserControllerImp {
  private final UserService userService;

  public UserControllerImp(UserService userService) {
    this.userService = userService;
  }

  /**
   * Endpoint para verificar el estado del servicio.
   *
   * @return "OK" si el servicio está funcionando correctamente.
   */
  @GetMapping("/health")
  @SwaggerApiResponses
  @Operation(
      summary = "Verifica el estado del servicio",
      description = "Devuelve 'OK' si el servicio está funcionando correctamente.")
  public String healthCheck() {
    return "OK";
  }

  /**
   * Endpoint para registrar un nuevo usuario.
   *
   * @param UserRequestDTO objeto que contiene la información del usuario a registrar.
   * @return el usuario registrado.
   */
  @PostMapping("/register")
  @SwaggerApiResponses
  @Operation(
      summary = "Registra un nuevo usuario",
      description =
          "Registra un nuevo usuario con los datos proporcionados y devuelve el token JWT del uusuario.")
  public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDTO userDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDTO));
  }

  /**
   * Endpoint para iniciar sesión de un usuario.
   *
   * @param loginRequestDto objeto que contiene la información del usuario que intenta iniciar
   *     sesión.
   * @return un mensaje indicando el resultado del intento de inicio de sesión.
   */
  @PostMapping("/login")
  @SwaggerApiResponses
  @Operation(
      summary = "Inicia sesión de un usuario",
      description =
          "Inicia sesión de un usuario con los datos proporcionados y devuelve el token JWT del usuario.")
  public ResponseEntity<String> logginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
    return ResponseEntity.ok(userService.loginUser(loginRequestDto));
  }

  /**
 * Endpoint to register a new admin user.
 *
 * Allows the creation of a new admin user in the system.
 *
 * @param userDTO the DTO containing the information of the admin user to register
 * @return ResponseEntity with a message indicating the result of the registration
 */
  @PostMapping("/register/admin")
  public ResponseEntity<String> registerAdmin(@Valid @RequestBody UserRequestDTO userDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerAdmin(userDTO));
  }

    /**
   * Endpoint to retrieve all users in the system.
   *
   * Accessible by users with ADMIN or USER roles.
   *
   * @return ResponseEntity containing a list of all user DTOs.
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/users")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  /**
   * Endpoint to obtain a user by email.
   *
   * Accessible by users with ADMIN or USER roles.
   *
   * @param email the email of the user to retrieve
   * @return ResponseEntity containing the user DTO.
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/user/{email}")
  public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  /**
   * Endpoint to delete a user by their UUID.
   *
   * Accessible only by users with ADMIN role.
   *
   * @param userId the UUID of the user to delete
   * @return ResponseEntity with no content if deletion is successful.
   */
  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    UUID uuid = UUID.fromString(userId);
    userService.deleteUser(uuid);
    return ResponseEntity.noContent().build();
  }
}
