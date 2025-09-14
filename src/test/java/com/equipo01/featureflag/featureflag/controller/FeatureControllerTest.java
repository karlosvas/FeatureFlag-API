package com.equipo01.featureflag.featureflag.controller;

import com.equipo01.featureflag.featureflag.controller.impl.FeatureControllerImp;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.service.FeatureService;
import com.equipo01.featureflag.featureflag.exception.GlobalExceptionHandler;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.UUID;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FeatureControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FeatureService featureService;

    @InjectMocks
    private FeatureControllerImp featureController;

    private ObjectMapper objectMapper;

    private UUID featureId;
    private FeatureToggleRequestDto toggleRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

       
        mockMvc = MockMvcBuilders.standaloneSetup(featureController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        featureId = UUID.randomUUID();
        toggleRequestDto = FeatureToggleRequestDto.builder()
                .clientId("clienteA")
                .environment(null)
                .build();
    }

    @Test
    void enableFeatureForClientOrEnvironment_success() throws Exception {
        mockMvc.perform(put("/api/features/" + featureId + "/enable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toggleRequestDto)))
                .andExpect(status().isNoContent());

        verify(featureService, times(1)).updateFeatureForClientOrEnvironment(featureId, toggleRequestDto, true);
    }

    @Test
    void disableFeatureForClientOrEnvironment_success() throws Exception {
        mockMvc.perform(put("/api/features/" + featureId + "/disable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toggleRequestDto)))
                .andExpect(status().isNoContent());

        verify(featureService, times(1)).updateFeatureForClientOrEnvironment(featureId, toggleRequestDto, false);
    }

    @Test
    void enableFeature_featureConfigNotFound() throws Exception {
        doThrow(new FeatureFlagException(HttpStatus.NOT_FOUND, MessageError.FEATURE_CONFIG_NOT_FOUND.getMessage(), MessageError.FEATURE_CONFIG_NOT_FOUND.getDescription()))
                .when(featureService).updateFeatureForClientOrEnvironment(featureId, toggleRequestDto, true);

        mockMvc.perform(put("/api/features/" + featureId + "/enable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toggleRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("FEATURE CONFIG NOT FOUND"))
                .andExpect(jsonPath("$.description").value("No matching configuration found for the given clientId or environment."));
    }

    @Test
    void disableFeature_featureConfigNotFound() throws Exception {
        doThrow(new FeatureFlagException(HttpStatus.NOT_FOUND, MessageError.FEATURE_CONFIG_NOT_FOUND.getMessage(), MessageError.FEATURE_CONFIG_NOT_FOUND.getDescription()))
                .when(featureService).updateFeatureForClientOrEnvironment(featureId, toggleRequestDto, false);

        mockMvc.perform(put("/api/features/" + featureId + "/disable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toggleRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("FEATURE CONFIG NOT FOUND"))
                .andExpect(jsonPath("$.description").value("No matching configuration found for the given clientId or environment."));
    }
}