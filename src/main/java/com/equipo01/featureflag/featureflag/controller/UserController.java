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
   * Endpoint to check the health status of the service.
   *
   * @return "OK" if the service is functioning correctly.
   */
  ResponseEntity<String> healthCheck();

  /**
   * Endpoint to register a new user.
   *
   * @param userDTO object containing the information of the user to register.
   * @return the registered user.
   */
  ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDTO userDTO);

  /**
   * Endpoint to log in a user.
   *
   * @param loginRequestDto object containing the information of the user attempting to log in.
   * @return a message indicating the result of the login attempt.
   */
  ResponseEntity<String> logginUser(@Valid @RequestBody LoginRequestDto loginRequestDto);

  /**
   * Endpoint to retrieve all users in the system.
   *
   * @return ResponseEntity containing a list of all user DTOs.
   */
  ResponseEntity<List<UserDTO>> getAllUsers();

  /**
   * Endpoint to obtain a user by email.
   *
   * @return ResponseEntity containing the user DTO.
   */
  ResponseEntity<UserDTO> getUserByEmail();

  /**
   * Endpoint to delete a user by their UUID.
   *
   * @param id the UUID of the user to delete
   * @return ResponseEntity with no content if deletion is successful.
   */
  ResponseEntity<Void> deleteUser(@PathVariable String id);

  /**
   * Endpoint to register a new admin user.
   *
   * @param userDTO the DTO containing the information of the admin user to register
   * @return ResponseEntity with a message indicating the result of the registration
   */
  ResponseEntity<String> registerAdmin(@Valid @RequestBody UserRequestDTO userDTO);
}
