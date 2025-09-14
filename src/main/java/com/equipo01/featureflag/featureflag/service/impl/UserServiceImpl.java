package com.equipo01.featureflag.featureflag.service.impl;

import com.equipo01.featureflag.featureflag.config.JwtUtil;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.UserMapper;
import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.model.enums.Role;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import com.equipo01.featureflag.featureflag.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios. Utiliza el repositorio
 * UserRepository para interactuar con la base de datos. Utiliza UserMapper para convertir entre
 * entidades y DTOs. Utiliza JwtUtil para generar tokens JWT. Utiliza SecurityConfig para el cifrado
 * de contraseñas. Utiliza AuthenticationManager para gestionar la autenticación de usuarios.
 * Utiliza Logger para registrar información y errores en el servicio.
 *
 * <p>Anotaciones utilizadas: - {@link Service} Anotación de Spring que indica que esta clase es un
 * servicio.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;
  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  /**
   * Registra un nuevo usuario en el sistema.
   *
   * @param userRequestDTO el DTO del usuario que contiene la información necesaria para el registro
   *     y login
   * @return un token JWT si el registro es exitoso
   */
  @Transactional
  public String registerUser(UserRequestDTO userRequestDTO) {
    checkRegister(userRequestDTO.getEmail(), userRequestDTO.getUsername());
    // By default, it assigns the User role
    UserDTO newUserDTO = userMapper.defaultUserDto(userRequestDTO);

    User user = userMapper.userDTOToUser(newUserDTO);
    user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
    userRepository.save(user);

    logger.info("User registered: {}", user.getEmail());

    Authentication authentication =
        buildAuthentication(newUserDTO.getUsername(), userRequestDTO.getPassword());

    return jwtUtil.generateToken(authentication);
  }

  /**
   * Endpoint to register a new admin user.
   *
   * @param userRequestDTO the DTO containing the information of the admin user to register
   * @return a token JWT if the registration is successful
   */
  @Transactional
  public String registerAdmin(UserRequestDTO userRequestDTO) {
    checkRegister(userRequestDTO.getEmail(), userRequestDTO.getUsername());

    // By default, it assigns the User role because immediately after we set the role to ADMIN
    UserDTO newUserDTO = userMapper.defaultUserDto(userRequestDTO);
    newUserDTO.setRole(Role.ADMIN);

    // Map the UserDTO to User entity, encode the password, and save the user
    User user = userMapper.userDTOToUser(newUserDTO);
    user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
    userRepository.save(user);

    logger.info("Admin user registered: {}", user.getEmail());

    // Build authentication and generate JWT token
    Authentication authentication =
        buildAuthentication(newUserDTO.getUsername(), userRequestDTO.getPassword());

    return jwtUtil.generateToken(authentication);
  }

  /**
   * Inicia sesión de un usuario con las credenciales proporcionadas. Este método autentica al
   * usuario usando sus credenciales (nombre y contraseña) y, si la autenticación es exitosa, genera
   * y devuelve un token JWT.
   *
   * @param loginRequestDto el DTO del usuario que contiene la información necesaria para el
   *     registro y login
   * @return un token JWT si la autenticación es exitosa
   */
  @Transactional(readOnly = true)
  public String loginUser(LoginRequestDto loginDto) {
    checkLogin(loginDto);
    Authentication authentication =
        buildAuthentication(loginDto.getUsername(), loginDto.getPassword());
    return jwtUtil.generateToken(authentication);
  }

  /**
   * Verify if a user with the given email or username already exists.
   *
   * @param email the email of the user
   * @param username the username of the user
   */
  @Override
  public void checkRegister(String email, String username) {
    // If user with email exists, throw exception
    if (userRepository.findByEmail(email).isPresent()) {
      throw new FeatureFlagException(
          MessageError.EMAIL_ALREADY_EXISTS.getStatus(),
          MessageError.EMAIL_ALREADY_EXISTS.getMessage(),
          MessageError.EMAIL_ALREADY_EXISTS.getDescription());
    }
    // If user with username exists, throw exception
    if (userRepository.findByUsername(username).isPresent()) {
      throw new FeatureFlagException(
          MessageError.USERNAME_ALREADY_EXISTS.getStatus(),
          MessageError.USERNAME_ALREADY_EXISTS.getMessage(),
          MessageError.USERNAME_ALREADY_EXISTS.getDescription());
    }
  }

  /**
   * Verify if a user with the given username exists and if the password is correct.
   *
   * @param loginRequestDto the login request DTO containing the username and password of the user
   * @throws FeatureFlagException if the username does not exist or the password is incorrect
   */
  @Override
  public void checkLogin(LoginRequestDto loginRequestDto) {
    // If user with username does not exist, throw exception
    User user = findByUsername(loginRequestDto.getUsername());
    // If password does not match, throw exception
    if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
      throw new FeatureFlagException(
          MessageError.INVALID_PASSWORD.getStatus(),
          MessageError.INVALID_PASSWORD.getMessage(),
          MessageError.INVALID_PASSWORD.getDescription());
    }
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user
   * @return an Optional containing the user if found, empty otherwise
   */
  @Override
  public User findByEmail(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      throw new FeatureFlagException(
          MessageError.USER_NOT_FOUND.getStatus(),
          MessageError.USER_NOT_FOUND.getMessage(),
          MessageError.USER_NOT_FOUND.getDescription());
    }
    return user.get();
  }

  /**
   * Retrieves a user by their username.
   *
   * @param username the username of the user
   * @return an Optional containing the user if found, empty otherwise
   */
  @Override
  public User findByUsername(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new FeatureFlagException(
          MessageError.USER_NOT_FOUND.getStatus(),
          MessageError.USER_NOT_FOUND.getMessage(),
          MessageError.USER_NOT_FOUND.getDescription());
    }
    return user.get();
  }

  /**
   * Builds an Authentication object using the provided username and password.
   *
   * @param username the username of the user
   * @param password the password of the user
   * @return an Authentication object containing the username and password
   */
  @Override
  public Authentication buildAuthentication(String username, String password) {
    return new UsernamePasswordAuthenticationToken(username, password);
  }

  @Override
  public Boolean existsByClientID(UUID clientID) {
    if (!userRepository.existsById(clientID)) {
      throw new FeatureFlagException(
          MessageError.USER_NOT_FOUND.getStatus(),
          MessageError.USER_NOT_FOUND.getMessage(),
          MessageError.USER_NOT_FOUND.getDescription());
    }
    return true;
  }

  /**
   * Obtain a user by email.
   *
   * @param email the email of the user
   * @return UserDTO the user DTO
   */
  public UserDTO getUserByEmail(String email) {
    // Find the user entity by email, throws exception if not found
    User user = this.findByEmail(email);
    // Map the User entity to UserDTO and return
    return userMapper.userToUserDTO(user);
  }

  /**
   * Retrieve all users in the system.
   *
   * @return List<UserDTO> list of all user DTOs
   */
  public List<UserDTO> getAllUsers() {
    // Find all user entities and map them to a list of UserDTOs
    return userMapper.userListToUserDTOList(userRepository.findAll());
  }

  /**
   * Delete a user by their UUID.
   *
   * <p>{link @Transactional} Ensures that the delete operation is executed within a transaction.
   *
   * @param userId the UUID of the user to delete
   * @throws FeatureFlagException if the user does not exist
   */
  @Transactional
  public void deleteUser(UUID userId) {
    // Check if the user exists before attempting to delete
    if (userRepository.existsById(userId)) {
      // Delete the user by their ID
      userRepository.deleteById(userId);
    } else {
      // Throw a custom exception if the user is not found
      throw new FeatureFlagException(
          MessageError.USER_NOT_FOUND.getStatus(),
          MessageError.USER_NOT_FOUND.getMessage(),
          MessageError.USER_NOT_FOUND.getDescription());
    }
  }
}
