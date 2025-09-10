package com.equipo01.featureflag.featureflag.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.equipo01.featureflag.featureflag.model.User;
import com.equipo01.featureflag.featureflag.repository.UserRepository;

/**
 * Servicio personalizado para cargar los detalles del usuario.
 * Extiende {@link UserDetailsService} e implementa el método
 * loadUserByUsername para buscar usuarios en la base de datos.
 * -Utiliza {@link UserRepository} para acceder a los datos de los usuarios.
 * 
 * @author alex
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // Repositorio para acceder a los datos de los usuarios
    private final UserRepository userRepository;

    /**
     * Constructor para inicializar las dependencias del servicio
     * 
     * @param userRepository
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga un usuario por su nombre de usuario.
     * -1.Busca el usuario en la base de datos usando userRepository.
     * -2.Si el usuario no existe lanzda una excepción UsernameNotFoundException.
     * -3.Si el usuario existe, convierte el objeto User en un UserDetails de Spring
     * Security.
     *
     * @param username nombre de usuario a buscar
     * @throws UsernameNotFoundException si el usuario no existe
     * @return un objeto UserDetails con la información del usuario
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario con nombre " + username + " no encontrado"));

        return org.springframework.security.core.userdetails.User.builder()
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .roles(userEntity.getRole().toString())
            .disabled(!userEntity.isEnabled())
            .build();
    }

}
