package com.equipo01.featureflag.featureflag.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.equipo01.featureflag.featureflag.config.JwtUtil;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.mapper.UserMapper;
import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @Mock private JwtUtil jwtUtil;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private UserServiceImpl userService;

  @Value("${api.auth}")
  private String authEndpoint;

  @Test
  void testRegisterUser() {
    UserRequestDTO userRequestDTO = mock(UserRequestDTO.class);
    User userEntity = mock(User.class);
    UserDTO userDTO = mock(UserDTO.class);
    User expectedUser = mock(User.class);

    when(userMapper.defaultUserDto(userRequestDTO)).thenReturn(userDTO);
    when(userMapper.userDTOToUser(userDTO)).thenReturn(userEntity);
    when(userRepository.save(userEntity)).thenReturn(expectedUser);
    when(jwtUtil.generateToken(any())).thenReturn("mocked-jwt-token");
    String token = userService.registerUser(userRequestDTO);
    verify(userRepository, times(1)).save(userEntity);
    verify(jwtUtil, times(1)).generateToken(any());
    assertEquals("mocked-jwt-token", token);
  }

  @Test
  void testLoginUser() {
    LoginRequestDto loginRequestDto = mock(LoginRequestDto.class);
    User user = mock(User.class);
    when(loginRequestDto.getUsername()).thenReturn("testuser");
    when(loginRequestDto.getPassword()).thenReturn("testpassword");
    when(user.getPassword()).thenReturn("testpassword");

    when(userRepository.findByUsername(loginRequestDto.getUsername()))
        .thenReturn(Optional.of(user));
    when(jwtUtil.generateToken(any())).thenReturn("mocked-jwt-token");
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    String token = userService.loginUser(loginRequestDto);
    verify(jwtUtil, times(1)).generateToken(any());
    assertEquals("mocked-jwt-token", token);
  }

  @Test
  void testCheckRegister_throwsFeatureFlagException_whenEmailExists() {
    String email = "test";
    String username = "testuser";
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> userService.checkRegister(email, username));
    verify(userRepository, times(1)).findByEmail(anyString());
    assertEquals(MessageError.EMAIL_ALREADY_EXISTS.getMessage(), result.getMessage());
  }

  @Test
  void testCheckRegister_throwsFeatureFlagException_whenUsernameExists() {
    String email = "test";
    String username = "testuser";
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));

    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> userService.checkRegister(email, username));
    verify(userRepository, times(1)).findByEmail(anyString());
    assertEquals(MessageError.USERNAME_ALREADY_EXISTS.getMessage(), result.getMessage());
  }

  @Test
  void testExistsByClientID_throwsFeatureFlagException_whenClientIDNotExists() {
    String clientID = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID cliUuid = UUID.fromString(clientID);
    when(userRepository.existsById(cliUuid)).thenReturn(Boolean.FALSE);

    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> userService.existsByClientID(cliUuid));
    verify(userRepository, times(1)).existsById(any(UUID.class));
    assertEquals(MessageError.USER_NOT_FOUND.getMessage(), result.getMessage());
  }

  @Test
  void testExistsByClientID() {
    String clientID = "44dc4cdb-aed4-4c55-8c9b-f1751faf47f9";
    UUID cliUuid = UUID.fromString(clientID);
    when(userRepository.existsById(cliUuid)).thenReturn(Boolean.TRUE);

    boolean result = userService.existsByClientID(cliUuid);
    verify(userRepository, times(1)).existsById(any(UUID.class));
    assertTrue(result);
  }
}
