package com.equipo01.featureflag.featureflag.service;

import com.equipo01.featureflag.featureflag.dto.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;

/**
 * Interface for user services.
 * Defines the operations available for user management.
 */
public interface UserService {
    /**
     * Registers a new user in the system.
     *
     * @param userRequestDTO the user's DTO containing the information
     *                       required for registration and login
     * @return a JWT token if registration is successful
     */
    public String registerUser(UserRequestDTO userDTO);
        

    /**
     * Logs in a user with the provided credentials.
     * This method authenticates the user using their credentials (username and
     * password) and,
     * if authentication is successful, generates and returns a JWT token.
     *
     * @param loginRequestDto the user's DTO containing the information
     *                       required for registration and login
     * @return a JWT token if authentication is successful
     */
    public String loginUser(LoginRequestDto loginRequestDto);
}