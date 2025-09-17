package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the User entity. Extends JpaRepository to provide CRUD and pagination operations.
 * Uses UUID as the identifier type.
 *
 * <p>Annotations used: - {@link Repository} Spring annotation indicating that this interface is a
 * repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
