package com.equipo01.featureflag.featureflag.service;

import org.springframework.stereotype.Service;
import com.equipo01.featureflag.featureflag.repository.UserRepository;

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios.
 * Utiliza el repositorio UserRepository para interactuar con la base de datos.
 * 
 * {@link Service} Anotación de Spring que indica que esta clase es un servicio.
 */
@Service
public class UserService {
     private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
