package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, UUID> {

}