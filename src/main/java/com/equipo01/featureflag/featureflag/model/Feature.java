package com.equipo01.featureflag.featureflag.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa una característica del sistema.
 * Esta entidad se utiliza para gestionar las características que pueden ser habilitadas o deshabilitadas en el sistema.
 *
 * Anotaciones utilizadas
 * - {@link Entity}: Indica que esta clase es una entidad JPA.
 * - {@link Table}: Especifica la tabla de la base de datos a la que está mapeada la entidad.
 * - {@link Getter}: Genera automáticamente los métodos getter para todos los campos de la clase.
 * - {@link Setter}: Genera automáticamente los métodos setter para todos los campos de la clase.
 * - {@link NoArgsConstructor}: Genera un constructor sin argumentos.
 * - {@link AllArgsConstructor}: Genera un constructor con todos los argumentos.
 * - {@link Builder}: Permite la construcción de objetos de esta clase utilizando el patrón Builder.
 * - {@link Column}: Se utiliza para especificar detalles sobre la columna de la base de datos.
 * - {@link OneToMany}: Define una relación de uno a muchos con otra entidad.
 * 
 * Atributos
 * - id: Identificador único de la característica.
 * - name: Nombre de la característica.
 * - description: Descripción de la característica.
 * - enableByDefault: Indica si la característica está habilitada por defecto.
 * - configs: Lista de configuraciones asociadas a la característica.
 */
@Entity
@Table(name = "features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Feature {
    /**
    * {@link Id} Indica el identificador único de la entidad.
    * {@link GeneratedValue} Especifica la estrategia de generación de valores para el identificador.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false, name = "feature_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false, name = "enabled_by_default")
    private Boolean enabledByDefault;

    // 1:N -> Una Feature tiene muchas configuraciones
    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FeatureConfig> configs;
}
