package com.equipo01.featureflag.featureflag.controller.impl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.dto.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;
import com.equipo01.featureflag.featureflag.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

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
public class UserControllerImp {
    private final UserService userService;

    public UserControllerImp(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para verificar el estado del servicio.
     *
     * @return "OK" si el servicio está funcionando correctamente.
     */
    @GetMapping("/health")
    @SwaggerApiResponses
    @Operation(summary = "Verifica el estado del servicio", description = "Devuelve 'OK' si el servicio está funcionando correctamente.")
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
    @SwaggerApiResponses
    @Operation(summary = "Registra un nuevo usuario", description = "Registra un nuevo usuario con los datos proporcionados y devuelve el token JWT del uusuario.")
    public String registerUser(@Valid @RequestBody UserRequestDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    /**
     * Endpoint para iniciar sesión de un usuario.
     *
     * @param loginRequestDto objeto que contiene la información del usuario que intenta iniciar sesión.
     * @return un mensaje indicando el resultado del intento de inicio de sesión.
     */
    @PostMapping("/login")
    @SwaggerApiResponses
    @Operation(summary = "Inicia sesión de un usuario", description = "Inicia sesión de un usuario con los datos proporcionados y devuelve el token JWT del usuario.")
    public String logginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return userService.loginUser(loginRequestDto);
    }
}
