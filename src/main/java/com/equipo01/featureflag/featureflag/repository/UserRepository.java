package com.equipo01.featureflag.featureflag.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.Optional;

/**
 * Repositorio para la entidad User.
 * Extiende JpaRepository para proporcionar operaciones CRUD y de paginación.
 * Utiliza UUID como tipo de identificador.
 * 
 * Anotaciones utilizadas:
 * - {@link Repository} Anotación de Spring que indica que esta interfaz es un repositorio.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
