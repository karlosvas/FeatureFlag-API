package com.equipo01.featureflag.featureflag.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.model.User;

/**
 * Interfaz para los servicios de usuario.
 * Define las operaciones disponibles para la gestión de usuarios.
 */
public interface UserService {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsername(String username);

    public void checkRegister(String email, String username);

    public void checkLogin(LoginRequestDto loginRequestDto);

    public Authentication buildAuthentication(String username, String password);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRequestDTO el DTO del usuario que contiene la información
     *                       necesaria para el registro y login
     * @return un token JWT si el registro es exitoso
     */
    public String registerUser(UserRequestDTO userDTO);
        

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
    public String loginUser(LoginRequestDto loginRequestDto);

    public Boolean existsByClientID(UUID clientID);
}