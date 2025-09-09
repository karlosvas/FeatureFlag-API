package com.equipo01.featureflag.featureflag.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.equipo01.featureflag.featureflag.config.JwtUtil;
import com.equipo01.featureflag.featureflag.config.SecurityConfig;
import com.equipo01.featureflag.featureflag.dto.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;
import com.equipo01.featureflag.featureflag.exception.UserAlreadyExistsException;
import com.equipo01.featureflag.featureflag.mapper.UserMapper;
import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.model.enums.Role;
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
    private final SecurityConfig securityConfig;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRequestDTO el DTO del usuario que contiene la información
     *                       necesaria para el registro y login
     * @return un token JWT si el registro es exitoso
     */
    public String registerUser(UserRequestDTO userDTO) {
            Optional<User> userByEmail = userRepository.findByEmail(userDTO.getEmail());
            Optional<User> userByUsername = userRepository.findByUsername(userDTO.getUsername());

            if (userByEmail.isPresent())
                throw new UserAlreadyExistsException("The email " + userDTO.getEmail() + " is already in use.");
            if (userByUsername.isPresent())
                throw new UserAlreadyExistsException("The username " + userDTO.getUsername() + " is already in use.");

            // Crea el user por defecto basandonos en la request
            UserDTO newUserDTO = UserDTO.builder()
                    .username(userDTO.getUsername())
                    .email(userDTO.getEmail())
                    .password(userDTO.getPassword())
                    .role(Role.USER)
                    .active(true)
                    .build();

            User user = userMapper.userDTOToUser(newUserDTO);

            user.setPassword(securityConfig.passwordEncoder().encode(userDTO.getPassword()));

            userRepository.save(user);
            logger.info("User registered: {}", user.getEmail());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(),
                    userDTO.getPassword());

        return jwtUtil.generateToken(authentication);

    }

    /**
     * Inicia sesión de un usuario con las credenciales proporcionadas.
     * Este método autentica al usuario usando sus credenciales (nombre y
     * contraseña) y,
     * si la autenticación es exitosa, genera y devuelve un token JWT.
     *
     * @param loginRequestDto el DTO del usuario que contiene la información
     *                       necesaria para el registro y login
     * @return un token JWT si la autenticación es exitosa
     */
    public String loginUser(LoginRequestDto loginRequestDto) {
        Optional<User> user = userRepository.findByUsername(loginRequestDto.getUsername());
        if (user.isEmpty()) 
            throw new BadCredentialsException("Invalid username");
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) 
            throw new BadCredentialsException("Invalid password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword());

        return jwtUtil.generateToken(authentication);
    }
}