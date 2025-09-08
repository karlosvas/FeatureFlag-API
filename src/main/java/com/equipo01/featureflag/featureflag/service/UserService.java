package com.equipo01.featureflag.featureflag.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;
import com.equipo01.featureflag.featureflag.exception.InvalidCredentialsException;
import com.equipo01.featureflag.featureflag.exception.UserAlreadyExistsException;
import com.equipo01.featureflag.featureflag.mapper.UserMapper;
import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.model.enums.Role;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.config.JwtUtil;
import com.equipo01.featureflag.featureflag.config.SecurityConfig;

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
 * - {@link Service} Anotación de Spring que indica que esta clase es un servicio.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil, SecurityConfig securityConfig, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.securityConfig = securityConfig;
        this.authenticationManager = authenticationManager;
    }

    /**
    * Registra un nuevo usuario en el sistema.
    *
    * @param userRequestDTO el DTO del usuario que contiene la información necesaria para el registro y login
    * @return un token JWT si el registro es exitoso
    */
    @SwaggerApiResponses
    public String registerUser(UserRequestDTO userDTO) {
        try {
            // Comprueba que no existiera previamente un usuario con el mismo email
            Optional<User> userByEmail = userRepository.findByEmail(userDTO.getEmail());
            Optional<User> userByUsername = userRepository.findByUsername(userDTO.getUsername());

            // Si el usuario ya existe, lanza una excepción ya que no puede haver registro duplicado
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
                
            // UserDTO -> User
            User user = userMapper.userDTOToUser(newUserDTO);
            
            // Cifra la contraseña
            user.setPassword(securityConfig.passwordEncoder().encode(userDTO.getPassword()));
            logger.info("Password encoded for user: " + user.getEmail());

            // Guarda en la base de datos el User
            userRepository.save(user);
            logger.info("User registered: " + user.getEmail());

            // Crea el objecto authentication
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(),
                    userDTO.getPassword()
                )
            );

            // Genera y devuelve JWT
            return jwtUtil.generateToken(authentication);
         } catch (UserAlreadyExistsException e) {
            logger.error("User already exists: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error with user registration: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
   
    /**
    * Inicia sesión de un usuario con las credenciales proporcionadas.
    * Este método autentica al usuario usando sus credenciales (nombre y contraseña) y,
    * si la autenticación es exitosa, genera y devuelve un token JWT.
    *
    * @param userRequestDTO el DTO del usuario que contiene la información necesaria para el registro y login
    * @return un token JWT si la autenticación es exitosa
    */
    @SwaggerApiResponses
    public String logginUser(UserRequestDTO userDTO)  {
        try {
            // Autenticar al usuario usando las credenciales proporcionadas
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(),      // Nombre de usuario proporcionado por el usuario
                    userDTO.getPassword()    // Contraseña proporcionada por el usuario
                )
            );
            
            // Si la autenticación es exitosa (no lanza excepción), genera un token JWT
            return jwtUtil.generateToken(authentication);
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for user: " + userDTO.getUsername());
            throw e; // <-- Esto sí lo capturará tu handler
        } catch (Exception e) {
            // Si las credenciales son inválidas o hay otro problema lo lanzamos
            logger.error("Error during login: " + e.getMessage(), e);
            throw new RuntimeException(e); 
        }
    }
}