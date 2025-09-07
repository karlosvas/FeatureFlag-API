package com.equipo01.featureflag.featureflag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;
import com.equipo01.featureflag.featureflag.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador para gestionar las operaciones relacionadas con los usuarios.
 * Utiliza el servicio UserService para realizar las operaciones de negocio.
 *
 * Proporciona endpoints para crear, actualizar y eliminar usuarios.
 * {@link RestController} Anotación de Spring que indica que esta clase es un
 * controlador REST.
 * {@link RequestMapping} Anotación de Spring que define la ruta base para
 * todos los endpoints en este controlador.
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para verificar el estado del servicio.
     */    
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }


    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param UserRequestDTO objeto que contiene la información del usuario a registrar.
     * @return el usuario registrado.
     */
    @PostMapping("/register")
    public String registerUser(@RequestBody UserRequestDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    /**
     * Endpoint para iniciar sesión de un usuario.
     *
     * @param userDTO objeto que contiene la información del usuario que intenta iniciar sesión.
     * @return un mensaje indicando el resultado del intento de inicio de sesión.
     */
    @PostMapping("/login")
    public String logginUser(@RequestBody UserRequestDTO userDTO) {
        return userService.logginUser(userDTO);
    }
}
