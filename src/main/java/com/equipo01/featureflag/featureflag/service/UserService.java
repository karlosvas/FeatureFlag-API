package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

/**
 * Interfaz para los servicios de usuario. Define las operaciones disponibles para la gestión de
 * usuarios.
 */
public interface UserService {

  public User findByEmail(String email);

  public User findByUsername(String username);

  public void checkRegister(String email, String username);

  public void checkLogin(LoginRequestDto loginRequestDto);

  public Authentication buildAuthentication(String username, String password);

  /**
   * Registra un nuevo usuario en el sistema.
   *
   * @param userRequestDTO el DTO del usuario que contiene la información necesaria para el registro
   *     y login
   * @return un token JWT si el registro es exitoso
   */
  public String registerUser(UserRequestDTO userDTO);

  /**
   * Inicia sesión de un usuario con las credenciales proporcionadas. Este método autentica al
   * usuario usando sus credenciales (nombre y contraseña) y, si la autenticación es exitosa, genera
   * y devuelve un token JWT.
   *
   * @param loginRequestDto el DTO del usuario que contiene la información necesaria para el
   *     registro y login
   * @return un token JWT si la autenticación es exitosa
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
