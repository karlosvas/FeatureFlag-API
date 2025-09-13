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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;

/**
 * Representa la configuración de un feature toggle. Esta clase contiene información sobre el estado
 * de un feature toggle en diferentes entornos. Esta clase es parte del modelo de datos de la
 * aplicación y se utiliza para almacenar la configuración de los feature toggles en la base de
 * datos.
 *
 * <p>Anotaciones utilizadas - {@link Entity} Anotación de JPA que indica que esta clase es una
 * entidad persistente. - {@link Table} Anotación de JPA que especifica el nombre de la tabla -
 * {@link NoArgsConstructor} Genera un constructor sin argumentos. - {@link AllArgsConstructor}
 * Genera un constructor con todos los argumentos. - {@link Getter} Genera los métodos get
 * automáticamente. - {@link Setter} Genera los métodos set automáticamente. - {@link Builder}
 * Genera el patrón builder para la clase - {@link Columns} Se utiliza para especificar detalles
 * sobre las columnas de la base de datos.
 *
 * <p>Atributos: - id: Identificador único de la configuración del feature toggle. - environment:
 * Entorno en el que se aplica la configuración (DEV, STAGING, PROD). - clientId: Identificador del
 * cliente al que pertenece la configuración. - enabled: Estado del feature toggle (habilitado o
 * deshabilitado). - feature: Referencia a la entidad Feature a la que pertenece esta configuración.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "feature_configs")
public class FeatureConfig {
  /**
   * {@link Id} Indica el identificador único de la entidad. {@link GeneratedValue} Especifica la
   * estrategia de generación de valores para el identificador.
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

  //  N:1 -> Muchas configuraciones pertenecen a una Feature
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "feature_id", referencedColumnName = "feature_id", nullable = false)
  private Feature feature;
}
