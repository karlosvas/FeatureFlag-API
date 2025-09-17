package com.equipo01.featureflag.featureflag.controller.impl;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.equipo01.featureflag.featureflag.dto.request.FeatureConfigRequestDto;
import com.equipo01.featureflag.featureflag.dto.response.FeatureConfigResponseDto;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.service.FeatureConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("staging")
class FeatureConfigControllerImpTest {

  @Value("${api.configurations}")
  private String configurationsEndpoint;

  @Autowired 
  private MockMvc mockMvc;

  @MockitoBean 
  private FeatureConfigService featureConfigService;

  private ObjectMapper objectMapper;
  private UUID featureConfigId;
  private FeatureConfigRequestDto requestDto;
  private FeatureConfigResponseDto responseDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();
    featureConfigId = UUID.randomUUID();
    
    // Setup test data
    requestDto = FeatureConfigRequestDto.builder()
        .featureId(UUID.randomUUID().toString())
        .environment(Environment.DEV)
        .clientId("test-client")
        .enabled(true)
        .build();
        
    responseDto = FeatureConfigResponseDto.builder()
        .id(featureConfigId)
        .featureId(UUID.fromString(requestDto.getFeatureId()))
        .environment(requestDto.getEnvironment())
        .clientId(requestDto.getClientId())
        .enabled(requestDto.getEnabled())
        .build();
  }

  // ===== PUT /enable-disable tests =====
  
  @Test
  void testSetFeatureEnabledOrDisabled_Success() throws Exception {
    List<FeatureConfigResponseDto> expectedResponse = Arrays.asList(responseDto);
    when(featureConfigService.enableOrDisableFeature(featureConfigId, true))
        .thenReturn(ResponseEntity.ok(expectedResponse));

    mockMvc.perform(put(configurationsEndpoint + "/enable-disable")
            .with(user("testuser").roles("USER"))
            .param("featureConfigId", featureConfigId.toString())
            .param("enable", "true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(featureConfigId.toString())))
        .andExpect(jsonPath("$[0].enabled", is(true)));

    verify(featureConfigService, times(1)).enableOrDisableFeature(featureConfigId, true);
  }

  @Test
  void testSetFeatureEnabledOrDisabled_Disable() throws Exception {
    FeatureConfigResponseDto disabledResponse = FeatureConfigResponseDto.builder()
        .id(featureConfigId)
        .featureId(UUID.fromString(requestDto.getFeatureId()))
        .environment(requestDto.getEnvironment())
        .clientId(requestDto.getClientId())
        .enabled(false)
        .build();
    
    List<FeatureConfigResponseDto> expectedResponse = Arrays.asList(disabledResponse);
    when(featureConfigService.enableOrDisableFeature(featureConfigId, false))
        .thenReturn(ResponseEntity.ok(expectedResponse));

    mockMvc.perform(put(configurationsEndpoint + "/enable-disable")
            .with(user("testuser").roles("USER")) 
            .param("featureConfigId", featureConfigId.toString())
            .param("enable", "false")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].enabled", is(false)));

    verify(featureConfigService, times(1)).enableOrDisableFeature(featureConfigId, false);
  }

  @Test
  void testSetFeatureEnabledOrDisabled_InvalidUUID() throws Exception {
    mockMvc.perform(put(configurationsEndpoint + "/enable-disable")
            .with(user("testuser").roles("USER"))
            .param("featureConfigId", "invalid-uuid")
            .param("enable", "true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testSetFeatureEnabledOrDisabled_MissingParameters() throws Exception {
    mockMvc.perform(put(configurationsEndpoint + "/enable-disable")
            .with(user("testuser").roles("USER")) 
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testSetFeatureEnabledOrDisabled_Unauthorized() throws Exception {
    mockMvc.perform(put(configurationsEndpoint + "/enable-disable")
            .param("featureConfigId", featureConfigId.toString())
            .param("enable", "true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  // ===== POST / tests =====
  @Test
  void testCreateFeatureConfig_Success() throws Exception {
    when(featureConfigService.createFeatureConfig(any(FeatureConfigRequestDto.class)))
        .thenReturn(responseDto);

    mockMvc.perform(post(configurationsEndpoint)
            .with(user("testuser").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(featureConfigId.toString())))
        .andExpect(jsonPath("$.environment", is("DEV")))
        .andExpect(jsonPath("$.clientId", is("test-client")))
        .andExpect(jsonPath("$.enabled", is(true)));

    verify(featureConfigService, times(1)).createFeatureConfig(any(FeatureConfigRequestDto.class));
  }

  @Test
  void testCreateFeatureConfig_WithAdminRole() throws Exception {
    when(featureConfigService.createFeatureConfig(any(FeatureConfigRequestDto.class)))
        .thenReturn(responseDto);

    mockMvc.perform(post(configurationsEndpoint)
            .with(user("admin").roles("ADMIN"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated());

    verify(featureConfigService, times(1)).createFeatureConfig(any(FeatureConfigRequestDto.class));
  }

  @Test
  void testCreateFeatureConfig_Unauthorized() throws Exception {
    mockMvc.perform(post(configurationsEndpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testCreateFeatureConfig_InvalidRequestBody() throws Exception {
    FeatureConfigRequestDto invalidRequest = FeatureConfigRequestDto.builder()
        .environment(Environment.DEV)
        // Missing required fields
        .build();

    mockMvc.perform(post(configurationsEndpoint)
            .with(user("testuser").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
  }

  // ===== GET /{id} tests =====
  
  @Test
  void testGetFeatureByID_Success() throws Exception {
    List<FeatureConfigResponseDto> expectedResponse = Arrays.asList(responseDto);
    when(featureConfigService.getFeatureByID(featureConfigId))
        .thenReturn(expectedResponse);

    mockMvc.perform(get(configurationsEndpoint + "/" + featureConfigId.toString())
            .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(featureConfigId.toString())));

    verify(featureConfigService, times(1)).getFeatureByID(featureConfigId);
  }

  @Test
  void testGetFeatureByID_WithAdminRole() throws Exception {
    List<FeatureConfigResponseDto> expectedResponse = Arrays.asList(responseDto);
    when(featureConfigService.getFeatureByID(featureConfigId))
        .thenReturn(expectedResponse);

    mockMvc.perform(get(configurationsEndpoint + "/" + featureConfigId.toString())
            .with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk());

    verify(featureConfigService, times(1)).getFeatureByID(featureConfigId);
  }

  @Test
  void testGetFeatureByID_Unauthorized() throws Exception {
    mockMvc.perform(get(configurationsEndpoint + "/" + featureConfigId.toString()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testGetFeatureByID_InvalidUUID() throws Exception {
    mockMvc.perform(get(configurationsEndpoint + "/invalid-uuid")
            .with(user("testuser").roles("USER")))
        .andExpect(status().isInternalServerError()); 
  }

  // ===== GET / tests =====
  
  @Test
  void testGetAllFeatures_Success() throws Exception {
    List<FeatureConfigResponseDto> expectedResponse = Arrays.asList(responseDto);
    when(featureConfigService.getAllFeatures()).thenReturn(expectedResponse);

    mockMvc.perform(get(configurationsEndpoint)
            .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(featureConfigId.toString())));

    verify(featureConfigService, times(1)).getAllFeatures();
  }

  @Test
  void testGetAllFeatures_WithAdminRole() throws Exception {
    List<FeatureConfigResponseDto> expectedResponse = Arrays.asList(responseDto);
    when(featureConfigService.getAllFeatures()).thenReturn(expectedResponse);

    mockMvc.perform(get(configurationsEndpoint)
            .with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk());

    verify(featureConfigService, times(1)).getAllFeatures();
  }

  @Test
  void testGetAllFeatures_Unauthorized() throws Exception {
    mockMvc.perform(get(configurationsEndpoint))
        .andExpect(status().isUnauthorized());
  }

  // ===== DELETE /{featureConfigId} tests =====
  
  @Test
  void testDeleteFeatureConfig_Success() throws Exception {
    doNothing().when(featureConfigService).deleteFeatureConfig(featureConfigId);

    mockMvc.perform(delete(configurationsEndpoint + "/" + featureConfigId.toString())
            .with(user("admin").roles("ADMIN")))
        .andExpect(status().isNoContent());

    verify(featureConfigService, times(1)).deleteFeatureConfig(featureConfigId);
  }

  @Test
  void testDeleteFeatureConfig_Unauthorized() throws Exception {
    mockMvc.perform(delete(configurationsEndpoint + "/" + featureConfigId.toString()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testDeleteFeatureConfig_Forbidden_UserRole() throws Exception {
    mockMvc.perform(delete(configurationsEndpoint + "/" + featureConfigId.toString())
            .with(user("user").roles("USER")))
        .andExpect(status().isForbidden());
  }

  @Test
  void testDeleteFeatureConfig_InvalidUUID() throws Exception {
    mockMvc.perform(delete(configurationsEndpoint + "/invalid-uuid")
            .with(user("admin").roles("ADMIN")))
        .andExpect(status().isInternalServerError()); 
  }
}
