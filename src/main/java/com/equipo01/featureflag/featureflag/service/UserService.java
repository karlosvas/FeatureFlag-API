package com.equipo01.featureflag.featureflag.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;
import com.equipo01.featureflag.featureflag.mapper.UserMapper;
import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.config.JwtUtil;
import com.equipo01.featureflag.featureflag.config.SecurityConfig;

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios.
 * Utiliza el repositorio UserRepository para interactuar con la base de datos.
 * 
 * {@link Service} Anotación de Spring que indica que esta clase es un servicio.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final SecurityConfig passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil, SecurityConfig passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
    * Registra un nuevo usuario en el sistema.
    *
    * @param userRequestDTO el DTO del usuario que contiene la información necesaria para el registro
    * @return un token JWT si el registro es exitoso
    */
    @SwaggerApiResponses
    public UserDTO registerUser(UserDTO userDTO) {
        try {
            // Comprobamos que no existiera previamente un usuario con el mismo email
            // Optional<User> userSearch = userRepository.findByEmail(userDTO.getEmail());

            // // Si el usuario ya existe, lanzamos una excepción ya que no puede haver registro duplicado
            // if(userSearch.isPresent())
            //     throw new Exception(); // TODO:: Manejar errores con error handler

            // // UserDTO -> User
            // User user = userMapper.userDTOToUser(userDTO);
            
            // // Ciframos la contraseña
            // user.setPassword(passwordEncoder.passwordEncoder(userDTO.getPassword()));
            // logger.info("Password encoded for user: " + user.getEmail());

            // // Guardar en la base de datos el User
            // userRepository.save(user);


            // // Creamos el objecto authentication
            // Authentication authentication = authenticationManager.authenticate(
            //     new UsernamePasswordAuthenticationToken(
            //         userDTO.getUsername(),
            //         userDTO.getPassword()
            //     )
            // );
            
            // Generar y devolver JWT 
            // return jwt.generateToken(authentication); TODO:: COnfiguracion de JWT user story 2
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario", e);
        }
        return null;
    }
   
    /**
    * Inicia sesión de un usuario con las credenciales proporcionadas.
     * Este método autentica al usuario usando sus credenciales (email y contraseña) y,
    * si la autenticación es exitosa, genera y devuelve un token JWT.
    * 
    * @param userDTO el DTO del usuario que contiene el email y la contraseña
    * @return un token JWT si la autenticación es exitosa
    */
    @SwaggerApiResponses
    public String logginUser(UserDTO userDTO)  {
        try {
            // Autenticar al usuario usando las credenciales proporcionadas
            // Authentication authentication = authenticationManager.authenticate(
            //     new UsernamePasswordAuthenticationToken(
            //         userDTO.getUsername(),      // Email proporcionado por el usuario
            //         userDTO.getPassword()    // Contraseña proporcionada por el usuario
            //     )
            // );
            
            // Si la autenticación es exitosa (no lanza excepción), genera un token JWT
            // return jwt.generateToken(authentication); // TODO: DEVOLBER TOKEN DE AUTENTIFICAION
            
        } catch (Exception e) {
            // Si las credenciales son inválidas o hay otro problema
             System.err.println("Error");  // TODO:: Manejar errores con error handler
        }
        return null;
    }
}