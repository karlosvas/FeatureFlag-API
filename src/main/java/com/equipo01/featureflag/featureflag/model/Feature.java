package com.equipo01.featureflag.featureflag.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a system feature. This entity is used to manage features that can be enabled or
 * disabled in the system. Representa una característica del sistema. Esta entidad se utiliza para
 * gestionar las características que pueden ser habilitadas o deshabilitadas en el sistema.
 *
 * <p>Annotations used - {@link Entity}: Indicates that this class is a JPA entity. - {@link Table}:
 * Specifies the database table to which the entity is mapped. - {@link Getter}: Automatically
 * generates getter methods for all fields in the class. - {@link Setter}: Automatically generates
 * setter methods for all fields in the class. - {@link NoArgsConstructor}: Generates a constructor
 * without arguments. - {@link AllArgsConstructor}: Generates a constructor with all arguments. -
 * {@link Builder}: Allows the construction of objects of this class using the Builder pattern. -
 * {@link Column}: Used to specify details about the database column. - {@link OneToMany}: Defines a
 * one-to-many relationship with another entity.
 *
 * <p>Attributes - id: Unique identifier of the feature. - name: Name of the feature. - description:
 * Description of the feature. - enableByDefault: Indicates whether the feature is enabled by
 * default. - configs: List of configurations associated with the feature.
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
   * {@link Id} Indicates the unique identifier of the entity. {@link GeneratedValue} Specifies the
   * value generation strategy for the identifier.
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

  // 1:N -> A feature can have multiple configurations
  @OneToMany(
      mappedBy = "feature",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<FeatureConfig> configs;
}
