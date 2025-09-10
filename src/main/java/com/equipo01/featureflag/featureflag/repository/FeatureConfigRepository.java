package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repositorio para gestionar la configuración de características (feature configurations) en la base de datos.
 * Extiende JpaRepository para proporcionar métodos CRUD y consultas personalizadas.
 * Las configuraciones de características se identifican de forma única mediante un UUID.
 *
 * Anotaciones utilizadas:
 * - {@link Repository} Anotación de Spring que indica que esta interfaz es un repositorio.
 */
@Repository
public interface FeatureConfigRepository extends JpaRepository<FeatureConfig, UUID> {
}
