package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

/**
 * Interface for user services.
 * Defines the operations available for user management.
 */
public interface UserService {

  public User findByEmail(String email);

  public User findByUsername(String username);

  public void checkRegister(String email, String username);

  public void checkLogin(LoginRequestDto loginRequestDto);

  public Authentication buildAuthentication(String username, String password);

    /**
     * Registers a new user in the system.
     *
     * @param userRequestDTO the user's DTO containing the information
     *                       required for registration and login
     * @return a JWT token if registration is successful
     */
  public String registerUser(UserRequestDTO userDTO);

  /**
   * Logs in a user with the provided credentials. This method authenticates the user using their
   * credentials (username and password) and, if authentication is successful, generates
   * and returns a JWT token.
   *
   * @param loginRequestDto the user's DTO containing the information required for login
   * @return a JWT token if authentication is successful
   */
  public String loginUser(LoginRequestDto loginRequestDto);

  public Boolean existsByClientID(UUID clientID);

  /**
   * Retrieve all users in the system.
   *
   * @return List<UserDTO> list of all user DTOs
   */
  public List<UserDTO> getAllUsers();

  /**
   * Obtain a user by email.
   *
   * @param email the email of the user
   * @return UserDTO the user DTO
   */
  public UserDTO getUserByEmail(String email);

  /**
   * Delete a user by their UUID.
   *
   * @param userId the UUID of the user to delete
   */
  public void deleteUser(UUID userId);

  /**
   * Register a new admin user.
   *
   * @param userDTO the DTO containing the information of the admin user to register
   * @return a message indicating the result of the registration
   */
  public String registerAdmin(UserRequestDTO userDTO);
}
