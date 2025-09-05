package com.equipo01.featureflag.featureflag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/auth")
public class UserController {
     private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String unsecured() {
        return "RUTA PUBLICA";
    }

    @GetMapping("/securizated")
    public String secured() {
        return "RUTA PROTEGIDA";
    }
    
    
}
