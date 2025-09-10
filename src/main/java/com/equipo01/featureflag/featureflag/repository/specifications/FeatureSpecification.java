package com.equipo01.featureflag.featureflag.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.equipo01.featureflag.featureflag.model.Feature;

/**
 * Specification class for building dynamic queries for the Feature entity.
 * <p>
 * This class provides methods to create specifications based on various
 * criteria such as name and enabledByDefault status.
 * </p>
 */
@Component
public class FeatureSpecification {

    /**
     * Creates a specification to filter features by name and enabledByDefault
     * status.
     *
     * @param name             the name filter (can be null)
     * @param enabledByDefault the enabled by default filter (can be null)
     * @return a Specification for filtering features
     */
    public Specification<Feature> hasNameOrEnabledByDefault(String name, Boolean enabledByDefault) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (enabledByDefault != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("enabledByDefault"), enabledByDefault));
            }

            return predicates;
        };
    }

    /**
     * Combines multiple specifications to filter features based on name and
     * enabledByDefault status.
     *
     * @param name             the name filter (can be null)
     * @param enabledByDefault the enabled by default filter (can be null)
     * @return a combined Specification for filtering features
     */
    public Specification<Feature> getFeatures(String name, Boolean enabledByDefault) {
        Specification<Feature> spec = hasNameOrEnabledByDefault(name, enabledByDefault);
        return Specification.<Feature>unrestricted()
                .and(spec);
    }
}
