package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para gestionar las características (features) en la base de datos.
 * Extiende JpaRepository para proporcionar métodos CRUD y consultas personalizadas.
 * Las características se identifican de forma única mediante un UUID.
 *
 * Anotaciones utilizadas:
 * - {@link Repository} Anotación de Spring que indica que esta interfaz es un repositorio.
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    boolean existsByName(String name);

    Optional<Feature> findByName(String name);
}