package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.Feature;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing features in the database.
 * Extends JpaRepository to provide CRUD methods and custom queries.
 * Features are uniquely identified by a UUID.
 *
 * Annotations used:
 * - {@link Repository} Spring annotation indicating that this interface is a repository.
 */
@Repository
public interface FeatureRepository
    extends JpaRepository<Feature, UUID>, JpaSpecificationExecutor<Feature> {

  boolean existsByName(String name);

  Optional<Feature> findByName(String name);
}
