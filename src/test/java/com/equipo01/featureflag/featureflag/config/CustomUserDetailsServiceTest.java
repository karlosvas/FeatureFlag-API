package com.equipo01.featureflag.featureflag.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.model.enums.Role;
import com.equipo01.featureflag.featureflag.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private CustomUserDetailsService customUserDetailsService;

  @Test
  void testLoadUserByUsername_returnsUserDetails_whenUserExists() {
    // Arrange
    String username = "testUser";
    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn(username);
    when(mockUser.getPassword()).thenReturn("password123");
    when(mockUser.getRole()).thenReturn(Role.USER);
    when(mockUser.isEnabled()).thenReturn(true);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

    // Act
    UserDetails result = customUserDetailsService.loadUserByUsername(username);

    // Assert
    assertNotNull(result);
    assertEquals(username, result.getUsername());
    assertEquals("password123", result.getPassword());
    assertTrue(result.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    assertTrue(result.isEnabled());
    verify(userRepository).findByUsername(username);
  }

  @Test
  void testLoadUserByUsername_returnsDisabledUser_whenUserIsDisabled() {
    // Arrange
    String username = "disabledUser";
    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn(username);
    when(mockUser.getPassword()).thenReturn("password123");
    when(mockUser.getRole()).thenReturn(Role.ADMIN);
    when(mockUser.isEnabled()).thenReturn(false);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

    // Act
    UserDetails result = customUserDetailsService.loadUserByUsername(username);

    // Assert
    assertNotNull(result);
    assertEquals(username, result.getUsername());
    assertEquals("password123", result.getPassword());
    assertTrue(result.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    assertFalse(result.isEnabled());
    verify(userRepository).findByUsername(username);
  }

  @Test
  void testLoadUserByUsername_throwsUsernameNotFoundException_whenUserNotFound() {
    // Arrange
    String username = "nonExistentUser";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act & Assert
    UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> customUserDetailsService.loadUserByUsername(username)
    );

    assertEquals("User with username " + username + " not found", exception.getMessage());
    verify(userRepository).findByUsername(username);
  }

  @Test
  void testLoadUserByUsername_handlesNullUsername() {
    // Arrange
    String username = null;
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act & Assert
    UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> customUserDetailsService.loadUserByUsername(username)
    );

    assertEquals("User with username " + username + " not found", exception.getMessage());
    verify(userRepository).findByUsername(username);
  }

  @Test
  void testLoadUserByUsername_handlesEmptyUsername() {
    // Arrange
    String username = "";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act & Assert
    UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> customUserDetailsService.loadUserByUsername(username)
    );

    assertEquals("User with username " + username + " not found", exception.getMessage());
    verify(userRepository).findByUsername(username);
  }
}