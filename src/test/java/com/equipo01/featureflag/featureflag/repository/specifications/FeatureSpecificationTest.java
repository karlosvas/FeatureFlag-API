package com.equipo01.featureflag.featureflag.repository.specifications;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.equipo01.featureflag.featureflag.model.Feature;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

/**
 * Unit tests for {@link FeatureSpecification} class.
 * Tests the creation of JPA Criteria API specifications for filtering Feature entities.
 */
@ExtendWith(MockitoExtension.class)
class FeatureSpecificationTest {

  private FeatureSpecification featureSpecification;

  @Mock private Root<Feature> root;
  @Mock private CriteriaQuery<?> query;
  @Mock private CriteriaBuilder criteriaBuilder;
  @Mock private Predicate likePredicate;
  @Mock private Predicate equalPredicate;
  @Mock private Predicate conjunction;
  @Mock private Predicate finalPredicate;
  @Mock private Path<Object> namePath;
  @Mock private Path<Object> enabledByDefaultPath;
  @Mock private Expression<String> lowerExpression;

 @BeforeEach
  void setUp() {
    featureSpecification = new FeatureSpecification();

    // Setup básico
    lenient().when(criteriaBuilder.conjunction()).thenReturn(conjunction);
    lenient().when(root.get("name")).thenReturn(namePath);
    lenient().when(root.get("enabledByDefault")).thenReturn(enabledByDefaultPath);
    lenient().when(criteriaBuilder.lower(any(Expression.class))).thenReturn(lowerExpression);
    lenient().when(criteriaBuilder.like(any(Expression.class), anyString())).thenReturn(likePredicate);
    lenient().when(criteriaBuilder.equal(any(Expression.class), any())).thenReturn(equalPredicate);
    
    // Configuración de and() para todas las combinaciones
    lenient().when(criteriaBuilder.and(eq(conjunction), eq(equalPredicate)))
        .thenReturn(finalPredicate);
    lenient().when(criteriaBuilder.and(eq(conjunction), eq(likePredicate)))
        .thenReturn(finalPredicate);
    lenient().when(criteriaBuilder.and(eq(finalPredicate), eq(equalPredicate)))
        .thenReturn(finalPredicate);
    lenient().when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class)))
        .thenReturn(finalPredicate);
  }

  @Test
  void testHasNameOrEnabledByDefault_WithEnabledByDefaultOnly() {
    // Arrange
    String name = null;
    Boolean enabledByDefault = false;

    // Configuración específica para este test
    when(criteriaBuilder.conjunction()).thenReturn(conjunction);
    when(root.get("enabledByDefault")).thenReturn(enabledByDefaultPath);
    when(criteriaBuilder.equal(enabledByDefaultPath, enabledByDefault)).thenReturn(equalPredicate);
    when(criteriaBuilder.and(conjunction, equalPredicate)).thenReturn(finalPredicate);

    // Act
    Specification<Feature> specification = featureSpecification.hasNameOrEnabledByDefault(name, enabledByDefault);
    Predicate result = specification.toPredicate(root, query, criteriaBuilder);

    // Assert
    assertNotNull(result, "Result should not be null");
    assertEquals(finalPredicate, result, "Result should be finalPredicate");
  }

  @Test
  void testHasNameOrEnabledByDefault_WithNameOnly() {
    // Arrange
    String name = "TestFeature";
    Boolean enabledByDefault = null;

    // Act
    Specification<Feature> specification = featureSpecification.hasNameOrEnabledByDefault(name, enabledByDefault);
    Predicate result = specification.toPredicate(root, query, criteriaBuilder);

    // Assert
    assertNotNull(result);
    verify(criteriaBuilder, atLeastOnce()).conjunction();
  }

  @Test
  void testHasNameOrEnabledByDefault_WithNullParameters() {
    // Arrange
    String name = null;
    Boolean enabledByDefault = null;

    // Act
    Specification<Feature> specification = featureSpecification.hasNameOrEnabledByDefault(name, enabledByDefault);
    Predicate result = specification.toPredicate(root, query, criteriaBuilder);

    // Assert
    assertNotNull(result);
    verify(criteriaBuilder, atLeastOnce()).conjunction();
  }

  @Test
  void testGetFeatures_ReturnsValidSpecification() {
    // Arrange
    String name = "TestFeature";
    Boolean enabledByDefault = true;

    // Act
    Specification<Feature> specification = featureSpecification.getFeatures(name, enabledByDefault);

    // Assert
    assertNotNull(specification);
  }

  @Test
  void testSpecification_IntegrationApproach() {
    // En lugar de mockear todo, vamos a probar que la especificación se crea correctamente
    String name = "TestFeature";
    Boolean enabledByDefault = true;

    // Act - solo probar que no lanza excepción
    assertDoesNotThrow(() -> {
        Specification<Feature> specification = 
            featureSpecification.hasNameOrEnabledByDefault(name, enabledByDefault);
        assertNotNull(specification);
    });
  }

  @Test
  void diagnostic_TestBasicMockSetup() {
    // Test 1: Verificar que conjunction funciona
    Predicate conjunctionResult = criteriaBuilder.conjunction();
    System.out.println("Conjunction result: " + conjunctionResult);
    assertNotNull(conjunctionResult);
    
    // Test 2: Verificar que and() funciona básicamente
    Predicate andResult = criteriaBuilder.and(conjunction, likePredicate);
    System.out.println("And result: " + andResult);
    assertNotNull(andResult);
    
    // Test 3: Probar la especificación sin parámetros
    Specification<Feature> spec = featureSpecification.hasNameOrEnabledByDefault(null, null);
    Predicate result = spec.toPredicate(root, query, criteriaBuilder);
    System.out.println("Null params result: " + result);
    assertNotNull(result);
  }

  @Test
  void testHasNameOrEnabledByDefault_CaseInsensitiveNameSearch() {
    // Arrange
    String name = "TESTFEATURE";
    Boolean enabledByDefault = null;

    // Act
    Specification<Feature> specification =
        featureSpecification.hasNameOrEnabledByDefault(name, enabledByDefault);
    specification.toPredicate(root, query, criteriaBuilder);

    // Assert
    verify(criteriaBuilder, times(1)).lower(any(Expression.class));
    verify(criteriaBuilder, times(1)).like(any(Expression.class), eq("%testfeature%")); // Should be lowercase
  }

  @Test
  void testGetFeatures_ReturnsUnrestrictedSpecification() {
    // Arrange
    String name = "TestFeature";
    Boolean enabledByDefault = true;

    // Act
    Specification<Feature> specification = featureSpecification.getFeatures(name, enabledByDefault);

    // Assert
    assertNotNull(specification);
    // The method should combine unrestricted specification with the filtered one
    // This test mainly verifies that the method executes without errors
    // and returns a non-null specification
  }

  /**
   * Tests the getFeatures method when both parameters are null.
   * Verifies that the method handles null inputs gracefully and returns
   * a specification that doesn't apply any filtering criteria.
   */
  @Test
  void testGetFeatures_WithNullParameters() {
    // Arrange
    String name = null;
    Boolean enabledByDefault = null;

    // Act
    Specification<Feature> specification = featureSpecification.getFeatures(name, enabledByDefault);

    // Assert
    assertNotNull(specification);
    // Should return a specification that doesn't filter anything when parameters are null
  }
}