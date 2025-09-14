package com.equipo01.featureflag.featureflag.controller;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {
  /**
   * Endpoint para verificar el estado del servicio.
   *
   * @return "OK" si el servicio está funcionando correctamente.
   */
  public ResponseEntity<String> healthCheck();

  /**
   * Endpoint para registrar un nuevo usuario.
   *
   * @param userDTO objeto que contiene la información del usuario a registrar.
   * @return el usuario registrado.
   */
  public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDTO userDTO);

  /**
   * Endpoint para iniciar sesión de un usuario.
   *
   * @param loginRequestDto objeto que contiene la información del usuario que intenta iniciar
   *     sesión.
   * @return un mensaje indicando el resultado del intento de inicio de sesión.
   */
  public ResponseEntity<String> logginUser(@Valid @RequestBody LoginRequestDto loginRequestDto);

   /**
   * Endpoint to retrieve all users in the system.
   *
   * @return ResponseEntity containing a list of all user DTOs.
   */
  public ResponseEntity<List<UserDTO>> getAllUsers();

  /**
   * Endpoint to obtain a user by email.
   *
   * @return ResponseEntity containing the user DTO.
   */
  public ResponseEntity<UserDTO> getUserByEmail();

  /**
   * Endpoint to delete a user by their UUID.
   *
   * @param id the UUID of the user to delete
   * @return ResponseEntity with no content if deletion is successful.
   */
  public ResponseEntity<Void> deleteUser(@PathVariable String id);

   /**
   * Endpoint to register a new admin user.
   *
   * @param userDTO the DTO containing the information of the admin user to register
   * @return ResponseEntity with a message indicating the result of the registration
   */
  public ResponseEntity<String> registerAdmin(@Valid @RequestBody UserRequestDTO userDTO);
}
