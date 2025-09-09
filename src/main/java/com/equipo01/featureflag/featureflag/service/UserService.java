package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;

/**
 * Interfaz para los servicios de usuario.
 * Define las operaciones disponibles para la gestión de usuarios.
 */
public interface UserService {
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
}