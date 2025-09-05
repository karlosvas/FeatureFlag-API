package com.equipo01.featureflag.featureflag.controller;

import org.springframework.web.bind.annotation.RestController;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.service.UserService;

/**
 * Controlador para gestionar las operaciones relacionadas con los usuarios.
 * Utiliza el servicio UserService para realizar las operaciones de negocio.
 * 
 * Proporciona endpoints para crear, actualizar y eliminar usuarios.
 * {@link RestController} Anotación de Spring que indica que esta clase es un controlador REST.
 */
@RestController
public class UserController {
     private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param userDTO objeto que contiene la información del usuario a registrar.
     * @return el usuario registrado.
     */
    public UserDTO registerUser(UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    /**
     * Endpoint para iniciar sesión de un usuario.
     *
     * @param userDTO objeto que contiene la información del usuario que intenta iniciar sesión.
     * @return un mensaje indicando el resultado del intento de inicio de sesión.
     */
    public String logginUser(UserDTO userDTO) {
        return userService.logginUser(userDTO);
    }
}
