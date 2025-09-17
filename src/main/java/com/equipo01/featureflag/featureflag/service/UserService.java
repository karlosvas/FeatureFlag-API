package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

/** Interface for user services. Defines the operations available for user management. */
public interface UserService {

  User findByEmail(String email);

  User findByUsername(String username);

  void checkRegister(String email, String username);

  void checkLogin(LoginRequestDto loginRequestDto);

  Authentication buildAuthentication(String username, String password);

  /**
   * Registers a new user in the system.
   *
   * @param userRequestDTO the user's DTO containing the information required for registration and
   *     login
   * @return a JWT token if registration is successful
   */
  String registerUser(UserRequestDTO userDTO);

  /**
   * Logs in a user with the provided credentials. This method authenticates the user using their
   * credentials (username and password) and, if authentication is successful, generates and returns
   * a JWT token.
   *
   * @param loginRequestDto the user's DTO containing the information required for login
   * @return a JWT token if authentication is successful
   */
  String loginUser(LoginRequestDto loginRequestDto);

  Boolean existsByClientID(UUID clientID);

  /**
   * Retrieve all users in the system.
   *
   * @return List<UserDTO> list of all user DTOs
   */
  List<UserDTO> getAllUsers();

  /**
   * Obtain a user by email.
   *
   * @param email the email of the user
   * @return UserDTO the user DTO
   */
  UserDTO getUserByEmail(String email);

  /**
   * Delete a user by their UUID.
   *
   * @param userId the UUID of the user to delete
   */
  void deleteUser(UUID userId);

  /**
   * Register a new admin user.
   *
   * @param userDTO the DTO containing the information of the admin user to register
   * @return a message indicating the result of the registration
   */
  String registerAdmin(UserRequestDTO userDTO);
}
