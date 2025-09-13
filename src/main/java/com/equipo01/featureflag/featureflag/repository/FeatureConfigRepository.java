package com.equipo01.featureflag.featureflag.repository;

import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar la configuración de características (feature configurations) en la
 * base de datos. Extiende JpaRepository para proporcionar métodos CRUD y consultas personalizadas.
 * Las configuraciones de características se identifican de forma única mediante un UUID.
 *
 * <p>Anotaciones utilizadas: - {@link Repository} Anotación de Spring que indica que esta interfaz
 * es un repositorio.
 */
@Repository
public interface FeatureConfigRepository extends JpaRepository<FeatureConfig, UUID> {}
