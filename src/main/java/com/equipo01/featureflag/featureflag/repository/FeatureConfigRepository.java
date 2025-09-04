package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface FeatureConfigRepository extends JpaRepository<FeatureConfig, UUID> {
}
