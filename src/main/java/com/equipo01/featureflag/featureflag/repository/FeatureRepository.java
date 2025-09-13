package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repository for managing features in the database.
 * Extends JpaRepository to provide CRUD methods and custom queries.
 * Features are uniquely identified by a UUID.
 *
 * Annotations used:
 * - {@link Repository} Spring annotation indicating that this interface is a repository.
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    boolean existsByName(String name);
}