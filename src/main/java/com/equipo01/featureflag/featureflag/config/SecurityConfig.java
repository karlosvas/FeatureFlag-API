package com.equipo01.featureflag.featureflag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil,
            CustomUserDetailsService userDetailsService) throws Exception {
        http
                // 2.1) Deshabilitar CSRF para APIs REST
                .csrf(csrf -> csrf.disable())
                // 2.2) Política de sesión (stateless para JWT)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 2.3) Rutas públicas vs protegidas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/securizated").authenticated()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())

                // 2.4) Agregar filtros personalizados
                .addFilter(new JwtAuthenticationFilter(
                        authenticationManager(userDetailsService), jwtUtil))
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

}
