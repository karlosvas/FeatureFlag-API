package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.Feature;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las características (features) en la base de datos. Extiende
 * JpaRepository para proporcionar métodos CRUD y consultas personalizadas. Las características se
 * identifican de forma única mediante un UUID.
 *
 * <p>Anotaciones utilizadas: - {@link Repository} Anotación de Spring que indica que esta interfaz
 * es un repositorio.
 */
@Repository
public interface FeatureRepository
    extends JpaRepository<Feature, UUID>, JpaSpecificationExecutor<Feature> {
  boolean existsByName(String name);

  Optional<Feature> findByName(String name);
}
