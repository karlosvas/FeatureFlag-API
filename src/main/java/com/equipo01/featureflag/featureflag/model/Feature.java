package com.equipo01.featureflag.featureflag.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false, name = "feature_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private boolean enableByDefault;

    // 1:N -> Una Feature tiene muchas configuraciones
    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FeatureConfig> configs = new ArrayList<>();

    }
