package com.equipo01.featureflag.featureflag.controller;

import org.springframework.web.bind.annotation.RestController;
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
}
