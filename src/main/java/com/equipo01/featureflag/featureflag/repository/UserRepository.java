package com.equipo01.featureflag.featureflag.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.Optional;

/**
 * Repository for the User entity.
 * Extends JpaRepository to provide CRUD and pagination operations.
 * Uses UUID as the identifier type.
 * 
 * Annotations used:
 * - {@link Repository} Spring annotation indicating that this interface is a repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
