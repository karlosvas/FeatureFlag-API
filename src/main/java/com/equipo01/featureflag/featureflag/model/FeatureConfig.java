package com.equipo01.featureflag.featureflag.model;

import com.equipo01.featureflag.featureflag.model.enums.Environment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;

/**
 * Represents the configuration of a feature toggle.
 * This class contains information about the state of a feature toggle in different environments.
 * This class is part of the application's data model and is used to store the configuration of feature toggles in the database.
 *
 * Annotations used
 * - {@link Entity} JPA annotation indicating that this class is a persistent entity.
 * - {@link Table} JPA annotation specifying the name of the table.
 * - {@link NoArgsConstructor} Generates a no-arguments constructor.
 * - {@link AllArgsConstructor} Generates a constructor with all arguments.
 * - {@link Getter} Generates getter methods automatically.
 * - {@link Setter} Generates setter methods automatically.
 * - {@link Builder} Generates the builder pattern for the class.
 * - {@link Columns} Used to specify details about the database columns.
 *
 * Attributes:
 * - id: Unique identifier of the feature toggle configuration.
 * - environment: Environment in which the configuration applies (DEV, STAGING, PROD).
 * - clientId: Identifier of the client to which the configuration belongs.
 * - enabled: State of the feature toggle (enabled or disabled).
 * - feature: Reference to the Feature entity to which this configuration belongs.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "feature_configs")
public class FeatureConfig {
    /**
    * {@link Id} Indicates the unique identifier of the entity.
    * {@link GeneratedValue} Specifies the value generation strategy for the identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
      columnDefinition = "uuid", 
      updatable = false, 
      nullable = false, 
      name = "feature_config_id")
    private UUID id;

  @Enumerated(EnumType.STRING) // DEV, STAGING, PROD
  @Column(nullable = false)
  private Environment environment;

  @Column(name = "client_id")
  private String clientId;

  @Column(nullable = false)
  private Boolean enabled;

    //  N:1 -> Many configurations can belong to one feature
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", referencedColumnName = "feature_id", nullable = false)
    private Feature feature;
}
