package com.equipo01.featureflag.featureflag.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class JwtAuthorizationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtil, customUserDetailsService);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void doFilterInternal_ShouldSetAuthentication_WhenValidTokenProvided() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";
        String bearerToken = "Bearer " + token;

        UserDetails userDetails = new User(username, "password", 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getUsernameFromJWT(token)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil).getUsernameFromJWT(token);
        verify(customUserDetailsService).loadUserByUsername(username);
        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenInvalidTokenProvided() throws ServletException, IOException {
        // Arrange
        String token = "invalid.jwt.token";
        String bearerToken = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil, never()).getUsernameFromJWT(token);
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenNoTokenProvided() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).validateToken(any());
        verify(jwtUtil, never()).getUsernameFromJWT(any());
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenAuthorizationHeaderWithoutBearer() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNzd29yZA==");

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).validateToken(any());
        verify(jwtUtil, never()).getUsernameFromJWT(any());
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenEmptyAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("");

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).validateToken(any());
        verify(jwtUtil, never()).getUsernameFromJWT(any());
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenBearerTokenIsEmpty() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer ");
        // El token será una cadena vacía "", no null
        when(jwtUtil.validateToken("")).thenReturn(false);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).validateToken(""); // Se llama con cadena vacía
        verify(jwtUtil, never()).getUsernameFromJWT(any());
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldContinueFilterChain_WhenExceptionOccursInUserDetailsService() 
        throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";
        String bearerToken = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getUsernameFromJWT(token)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username))
            .thenThrow(new RuntimeException("User service error"));

        // Act - NO debería lanzar excepción porque el filtro la captura
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert - La cadena de filtros DEBE continuar
        verify(filterChain, times(1)).doFilter(request, response);
        
        // Assert - NO debe establecerse autenticación debido al error
        verify(SecurityContextHolder.getContext(), never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_ShouldHandleEmptyToken_WhenBearerHasOnlySpace() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer ");
        // El token será una cadena vacía "", no null
        when(jwtUtil.validateToken("")).thenReturn(false);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).validateToken(""); // ✅ SÍ se llama con cadena vacía
        verify(jwtUtil, never()).getUsernameFromJWT(any());
        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
    }
}