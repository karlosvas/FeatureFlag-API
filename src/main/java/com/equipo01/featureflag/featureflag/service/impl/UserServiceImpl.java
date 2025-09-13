package com.equipo01.featureflag.featureflag.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equipo01.featureflag.featureflag.config.JwtUtil;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.UserMapper;
import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import com.equipo01.featureflag.featureflag.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios.
 * Utiliza el repositorio UserRepository para interactuar con la base de datos.
 * Utiliza UserMapper para convertir entre entidades y DTOs.
 * Utiliza JwtUtil para generar tokens JWT.
 * Utiliza SecurityConfig para el cifrado de contraseñas.
 * Utiliza AuthenticationManager para gestionar la autenticación de usuarios.
 * Utiliza Logger para registrar información y errores en el servicio.
 * 
 * Anotaciones utilizadas:
 * - {@link Service} Anotación de Spring que indica que esta clase es un
 * servicio.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRequestDTO el DTO del usuario que contiene la información
     *                       necesaria para el registro y login
     * @return un token JWT si el registro es exitoso
     */
    @Transactional
    public String registerUser(UserRequestDTO userRequestDTO) {
        checkRegister(userRequestDTO.getEmail(), userRequestDTO.getUsername());

        UserDTO newUserDTO = userMapper.defaultUserDto(userRequestDTO);

        User user = userMapper.userDTOToUser(newUserDTO);
        user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        userRepository.save(user);

        logger.info("User registered: {}", user.getEmail());

        Authentication authentication = buildAuthentication(newUserDTO.getUsername(), newUserDTO.getPassword());

        return jwtUtil.generateToken(authentication);

    }

    /**
     * Inicia sesión de un usuario con las credenciales proporcionadas.
     * Este método autentica al usuario usando sus credenciales (nombre y
     * contraseña) y,
     * si la autenticación es exitosa, genera y devuelve un token JWT.
     *
     * @param loginRequestDto el DTO del usuario que contiene la información
     *                        necesaria para el registro y login
     * @return un token JWT si la autenticación es exitosa
     */
    @Transactional(readOnly = true)
    public String loginUser(LoginRequestDto loginDto) {
        checkLogin(loginDto);
        Authentication authentication = buildAuthentication(loginDto.getUsername(), loginDto.getPassword());
        return jwtUtil.generateToken(authentication);
    }

    /**
     * Verify if a user with the given email or username already exists.
     *
     * @param email    the email of the user
     * @param username the username of the user
     */
    @Override
    public void checkRegister(String email, String username) {
        Optional<User> userByEmail = findByEmail(email);
        Optional<User> userByUsername = findByUsername(username);

        if (userByEmail.isPresent()) {
            throw new FeatureFlagException(
                    MessageError.EMAIL_ALREADY_EXISTS.getStatus(),
                    MessageError.EMAIL_ALREADY_EXISTS.getMessage(),
                    MessageError.EMAIL_ALREADY_EXISTS.getDescription());
        }
        if (userByUsername.isPresent()) {
            throw new FeatureFlagException(
                    MessageError.USERNAME_ALREADY_EXISTS.getStatus(),
                    MessageError.USERNAME_ALREADY_EXISTS.getMessage(),
                    MessageError.USERNAME_ALREADY_EXISTS.getDescription());
        }
    }

    /**
     * Verify if a user with the given username exists and if the password is
     * correct.
     *
     * @param loginRequestDto the login request DTO containing the username and
     *                        password
     *                        of the user
     * @throws FeatureFlagException if the username does not exist or the password
     *                              is incorrect
     */
    @Override
    public void checkLogin(LoginRequestDto loginRequestDto) {
        Optional<User> user = findByUsername(loginRequestDto.getUsername());
        if (user.isEmpty()) {
            throw new FeatureFlagException(
                    MessageError.INVALID_USERNAME.getStatus(),
                    MessageError.INVALID_USERNAME.getMessage(),
                    MessageError.INVALID_USERNAME.getDescription());
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) {
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
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return an Optional containing the user if found, empty otherwise
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
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
        return new UsernamePasswordAuthenticationToken(
                username,
                password);
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

    @Transactional
    public void deleteUser(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new FeatureFlagException(
                    MessageError.USER_NOT_FOUND.getStatus(),
                    MessageError.USER_NOT_FOUND.getMessage(),
                    MessageError.USER_NOT_FOUND.getDescription());
        }
    }
}