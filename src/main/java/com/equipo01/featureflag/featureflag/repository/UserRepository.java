package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad User. Extiende JpaRepository para proporcionar operaciones CRUD y de
 * paginación. Utiliza UUID como tipo de identificador.
 *
 * <p>Anotaciones utilizadas: - {@link Repository} Anotación de Spring que indica que esta interfaz
 * es un repositorio.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
