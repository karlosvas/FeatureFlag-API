package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repository for managing feature configurations in the database.
 * Extends JpaRepository to provide CRUD methods and custom queries.
 * Feature configurations are uniquely identified by a UUID.
 *
 * Annotations used:
 * - {@link Repository} Spring annotation indicating that this interface is a repository.
 */
@Repository
public interface FeatureConfigRepository extends JpaRepository<FeatureConfig, UUID> {
}
