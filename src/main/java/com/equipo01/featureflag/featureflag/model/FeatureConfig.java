package com.equipo01.featureflag.featureflag.model;

import com.equipo01.featureflag.featureflag.model.enums.Environment;
import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "feature_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false, name = "feature_config_id")
    private UUID id;

    @Enumerated(EnumType.STRING) //DEV, STAGING, PROD
    @Column(nullable = false)
    private Environment environment;

    @Column(name = "client_id")
    private String clientId;

    @Column(nullable = false)
    private boolean enabled;

    //  N:1 -> Muchas configuraciones pertenecen a una Feature
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;
}

