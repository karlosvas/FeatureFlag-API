package com.equipo01.featureflag.featureflag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import jakarta.validation.Valid;

public interface UserController {
    /**
     * Endpoint para verificar el estado del servicio.
     *
     * @return "OK" si el servicio está funcionando correctamente.
     */
    public ResponseEntity<String> healthCheck();

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param userDTO objeto que contiene la información del usuario a registrar.
     * @return el usuario registrado.
     */
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDTO userDTO);

    /**
     * Endpoint para iniciar sesión de un usuario.
     *
     * @param loginRequestDto objeto que contiene la información del usuario que intenta iniciar sesión.
     * @return un mensaje indicando el resultado del intento de inicio de sesión.
     */
    public ResponseEntity<String> logginUser(@Valid @RequestBody LoginRequestDto loginRequestDto);

    public ResponseEntity<Void> deleteUser(@PathVariable String id);
}
