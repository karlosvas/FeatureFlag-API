package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing feature configurations in the database. Extends JpaRepository to provide
 * CRUD methods and custom queries. Feature configurations are uniquely identified by a UUID.
 *
 * <p>Annotations used: - {@link Repository} Spring annotation indicating that this interface is a
 * repository.
 */
@Repository
public interface FeatureConfigRepository extends JpaRepository<FeatureConfig, UUID> {}
