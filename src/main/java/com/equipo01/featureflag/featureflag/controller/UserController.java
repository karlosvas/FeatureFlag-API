package com.equipo01.featureflag.featureflag.controller;

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
 * Controller for managing user-related operations.
 * Uses the UserService service to perform business operations.
 *
 * Provides endpoints to create, update, and delete users.
 * {@link RestController} Spring annotation indicating that this class is a
 * REST controller.
 * {@link RequestMapping} Spring annotation defining the base path for
 * all endpoints in this controller.
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to verify service status.
     */    
    @GetMapping("/health")
    @SwaggerApiResponses
    @Operation(summary = "Verify service status", description = "Returns 'OK' if the service is running correctly.")
    public String healthCheck() {
        return "OK";
    }


    /**
     * Endpoint to register a new user.
     *
     * @param UserRequestDTO object containing the information of the user to register.
     * @return the registered user.
     */
    @PostMapping("/register")
    @SwaggerApiResponses
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided data and returns the user's JWT token.")
    public String registerUser(@Valid @RequestBody UserRequestDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    /**
     * Endpoint to log in a user.
     *
     * @param loginRequestDto object containing the information of the user trying to log in.
     * @return a message indicating the result of the login attempt.
     */
    @PostMapping("/login")
    @SwaggerApiResponses
    @Operation(summary = "Log in a user", description = "Logs in a user with the provided data and returns the user's JWT token.")
    public String logginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return userService.loginUser(loginRequestDto);
    }
}
