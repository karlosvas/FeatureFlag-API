package com.equipo01.featureflag.featureflag.controller.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.equipo01.featureflag.featureflag.dto.LinkDto;
import com.equipo01.featureflag.featureflag.dto.LinksDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureResponseDto;
import com.equipo01.featureflag.featureflag.dto.response.GetFeatureResponseDto;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("staging")
@WithMockUser(username = "admin", password = "adminPassword123")
class FeatureControllerImpTest {

  @Value("${api.features}")
  private String featuresEndpoint;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private FeatureService featureService;

  private ObjectMapper objectMapper;
  private UUID featureId;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();
    featureId = UUID.randomUUID();
  }

  @Test
  void testGetFeature() throws Exception {
    FeatureResponseDto responseDto =
        FeatureResponseDto.builder()
            .id(featureId)
            .name("New Feature")
            .description("This is a new feature")
            .enabledByDefault(true)
            .build();

    when(featureService.getFeatureById(anyString())).thenReturn(responseDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(featuresEndpoint + "/{featureId}", featureId.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Feature"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This is a new feature"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.enabledByDefault").value(true));
  }

  @Test
  void testCreateFeature() throws Exception {
    FeatureResponseDto featureResponseDto =
        FeatureResponseDto.builder()
            .id(featureId)
            .name("Newly Created Feature")
            .description("This is a newly created feature")
            .enabledByDefault(true)
            .build();

    when(featureService.createFeature(any())).thenReturn(featureResponseDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(featuresEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(featureResponseDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Newly Created Feature"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.description")
                .value("This is a newly created feature"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.enabledByDefault").value(true));
  }

  @Test
  void testCreateFeature_throwsExceptionInvalidBody() throws Exception {
    FeatureResponseDto featureResponseDto =
        FeatureResponseDto.builder()
            .id(featureId)
            .name("")
            .description("")
            .enabledByDefault(null)
            .build();
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(featuresEndpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(featureResponseDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].message")
                    .value(MessageError.DTO_FIELDS_NOT_VALID.getMessage()))
            .andReturn();
    assertAll(
        () ->
            assertTrue(
                result.getResponse().getContentAsString().contains("Feature name is required")),
        () ->
            assertTrue(
                result
                    .getResponse()
                    .getContentAsString()
                    .contains("Feature description is required")),
        () ->
            assertTrue(
                result
                    .getResponse()
                    .getContentAsString()
                    .contains("Feature enabledByDefault status is required")));
  }

  @Test
  void testGetFeatures() throws Exception {
    GetFeatureResponseDto getFeatureResponseDto =
        GetFeatureResponseDto.builder()
            .features(
                List.of(
                    FeatureResponseDto.builder()
                        .id(featureId)
                        .name("Feature 1")
                        .description("Description 1")
                        .enabledByDefault(true)
                        .build()))
            .links(
                LinksDto.builder()
                    .count("1")
                    .first(new LinkDto("?name=Feature 1&enabled=true&page=0&size=10"))
                    .last(new LinkDto("?name=Feature 1&enabled=true&page=0&size=10"))
                    .next(null)
                    .prev(null)
                    .build())
            .build();

    when(featureService.getFeatures(anyString(), any(), anyInt(), anyInt()))
        .thenReturn(getFeatureResponseDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(featuresEndpoint)
                .param("name", "Feature 1")
                .param("enabled", "true")
                .param("page", "0")
                .param("size", "10"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.features").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.features[0].name").value("Feature 1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.features[0].description").value("Description 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.features[0].enabledByDefault").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.links.count").value("1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.links.first.href")
                .value("?name=Feature 1&enabled=true&page=0&size=10"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.links.last.href")
                .value("?name=Feature 1&enabled=true&page=0&size=10"));
  }

  @Test
  void testGetFeatures_throwsExceptionInvalidPageAndSize() throws Exception {

    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(featuresEndpoint)
                    .param("page", "-1")
                    .param("size", "-1"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].message")
                    .value(MessageError.VALIDATION_PARAMETER_NOT_VALID.getMessage()))
            .andReturn();
    assertAll(
        () ->
            assertTrue(
                result.getResponse().getContentAsString().contains("Page must be at least 0")),
        () ->
            assertTrue(
                result.getResponse().getContentAsString().contains("Size must be at least 1")));
  }

    @Test
  void testCheckPermissionTest() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(featuresEndpoint + "/test")
            .with(user("admin").roles("ADMIN")))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Test permission ok"));
  }

  @Test
  void testCheckPermissionTestWithoutAdminRole_shouldReturnForbidden() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(featuresEndpoint + "/test")
            .with(user("user").roles("USER")))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }
}
