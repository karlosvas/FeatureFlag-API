package com.equipo01.featureflag.featureflag.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QueryParamBuilderTest {

  @InjectMocks private QueryParamBuilder queryParamBuilder;

  @Test
  public void testBuildQueryFeatureWithAllParameters() {
    var queryParams = queryParamBuilder.buildQueryFeature("feature1", true);
    assertEquals("feature1", queryParams.get("name"));
    assertEquals("true", queryParams.get("enabled"));
  }

  @Test
  public void testBuildQueryFeatureWithNullName() {
    var queryParams = queryParamBuilder.buildQueryFeature(null, false);
    assertEquals("", queryParams.get("name"));
    assertEquals("false", queryParams.get("enabled"));
  }

  @Test
  public void testBuildQueryFeatureWithNullEnabledByDefault() {
    var queryParams = queryParamBuilder.buildQueryFeature("feature2", null);
    assertEquals("feature2", queryParams.get("name"));
    assertEquals("", queryParams.get("enabled"));
  }
}
