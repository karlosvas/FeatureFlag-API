package com.equipo01.featureflag.featureflag.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.equipo01.featureflag.featureflag.config.JwtUtil;
import com.equipo01.featureflag.featureflag.config.SecurityConfig;
import com.equipo01.featureflag.featureflag.dto.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.UserRequestDTO;
import com.equipo01.featureflag.featureflag.exception.UserAlreadyExistsException;
import com.equipo01.featureflag.featureflag.mapper.UserMapper;
import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.model.enums.Role;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import com.equipo01.featureflag.featureflag.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing user-related operations.
 * Uses the UserRepository repository to interact with the database.
 * Uses UserMapper to convert between entities and DTOs.
 * Uses JwtUtil to generate JWT tokens.
 * Uses SecurityConfig for password encryption.
 * Uses AuthenticationManager to manage user authentication.
 * Uses Logger to log information and errors in the service.
 * 
 * Annotations used:
 * - {@link Service} Spring annotation indicating that this class is a
 * service.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final SecurityConfig securityConfig;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Registers a new user in the system.
     *
     * @param userRequestDTO user's DTO containing the necessary information for
     *                       registration
     * @return a JWT token if registration is successful
     */
    public String registerUser(UserRequestDTO userDTO) {
            Optional<User> userByEmail = userRepository.findByEmail(userDTO.getEmail());
            Optional<User> userByUsername = userRepository.findByUsername(userDTO.getUsername());

            if (userByEmail.isPresent())
                throw new UserAlreadyExistsException("The email " + userDTO.getEmail() + " is already in use.");
            if (userByUsername.isPresent())
                throw new UserAlreadyExistsException("The username " + userDTO.getUsername() + " is already in use.");

            // Create the default user based on the request
            UserDTO newUserDTO = UserDTO.builder()
                    .username(userDTO.getUsername())
                    .email(userDTO.getEmail())
                    .password(userDTO.getPassword())
                    .role(Role.USER)
                    .active(true)
                    .build();

            User user = userMapper.userDTOToUser(newUserDTO);

            user.setPassword(securityConfig.passwordEncoder().encode(userDTO.getPassword()));

            userRepository.save(user);
            logger.info("User registered: {}", user.getEmail());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(),
                    userDTO.getPassword());

        return jwtUtil.generateToken(authentication);

    }

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
    public String loginUser(LoginRequestDto loginRequestDto) {
        Optional<User> user = userRepository.findByUsername(loginRequestDto.getUsername());
        if (user.isEmpty()) 
            throw new BadCredentialsException("Invalid username");
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) 
            throw new BadCredentialsException("Invalid password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword());

        return jwtUtil.generateToken(authentication);
    }
}