package com.equipo01.featureflag.featureflag.util;

import static org.junit.jupiter.api.Assertions.*;

import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BaseLinkBuilderTest {

  @InjectMocks private BaseLinkBuilder baseLinkBuilder;

  @Test
  public void testCreateBaseLink_throwsFeatureFlagException_whenQueryParamsIsNull() {
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> baseLinkBuilder.createBaseLink(null));
    assertEquals(MessageError.MAP_QUERY_PARAMS_NOT_VALID.getMessage(), result.getMessage());
  }

  @Test
  public void testCreateBaseLink_throwsFeatureFlagException_whenQueryParamsIsEmpty() {
    Map<String, String> queryParams = new HashMap<>();
    FeatureFlagException result =
        assertThrows(FeatureFlagException.class, () -> baseLinkBuilder.createBaseLink(queryParams));
    assertEquals(MessageError.MAP_QUERY_PARAMS_NOT_VALID.getMessage(), result.getMessage());
  }

  @Test
  public void testCreateBaseLink() {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("param1", "value1");
    queryParams.put("param2", "value2");

    String result = baseLinkBuilder.createBaseLink(queryParams);

    assertTrue(result.contains("param1=value1"));
    assertTrue(result.contains("param2=value2"));
  }
}
