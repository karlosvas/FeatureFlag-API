package com.equipo01.featureflag.featureflag.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.equipo01.featureflag.featureflag.model.Feature;
import com.equipo01.featureflag.featureflag.model.FeatureConfig;
import com.equipo01.featureflag.featureflag.model.enums.Environment;
import com.equipo01.featureflag.featureflag.dto.request.FeatureToggleRequestDto;
import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;
import com.equipo01.featureflag.featureflag.repository.FeatureConfigRepository;
import com.equipo01.featureflag.featureflag.repository.FeatureRepository;

public class FeatureServiceImplTest {
        @Mock
        private FeatureRepository featureRepository;

        @Mock
        private FeatureConfigRepository featureConfigRepository;

        @InjectMocks
        private FeatureServiceImpl featureService;

        private Feature feature;
        private FeatureConfig configDev;
        private FeatureConfig configProd;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);

                // Crear Feature y FeautureConfigs
                configDev = FeatureConfig.builder()
                                .id(UUID.randomUUID())
                                .environment(Environment.DEV)
                                .clientId("clienteA")
                                .enabled(false)
                                .build();

                configProd = FeatureConfig.builder()
                                .id(UUID.randomUUID())
                                .environment(Environment.PROD)
                                .clientId("clienteB")
                                .enabled(false)
                                .build();

                feature = Feature.builder()
                                .id(UUID.randomUUID())
                                .name("featureX")
                                .description("Test Feature")
                                .enabledByDefault(false)
                                .configs(Arrays.asList(configDev, configProd))
                                .build();

                configDev.setFeature(feature);
                configProd.setFeature(feature);

        }

        @Test
        void testEnabledFeatureForClientOrEnvironment_success() {
                when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));

                FeatureToggleRequestDto requestDto = FeatureToggleRequestDto.builder()
                                .clientId("clienteA")
                                .environment(null)
                                .build();

                featureService.enableFeatureForClientOrEnvironment(feature.getId(), requestDto);

                assertTrue(configDev.getEnabled());
                assertFalse(configProd.getEnabled()); // La otra configuración no debe cambiar

                verify(featureRepository, times(1)).save(feature);
        }

        @Test
        void testEnableFeatureForClientOrEnvironment_featureNotFound() {
                UUID randomId = UUID.randomUUID();
                when(featureRepository.findById(randomId)).thenReturn(Optional.empty());

                FeatureToggleRequestDto dto = FeatureToggleRequestDto.builder()
                                .clientId("clientA")
                                .build();

                assertThrows(FeatureFlagException.class,
                                () -> featureService.enableFeatureForClientOrEnvironment(randomId, dto));
        }

        @Test
        void testDisableFeatureForClientOrEnvironment_success() {
                // Mock repository to return the feature
                when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));

                // Crear DTO para deshabilitar configDev
                FeatureToggleRequestDto requestDto = FeatureToggleRequestDto.builder()
                                .clientId("clienteA")
                                .environment(null)
                                .build();

                // Primero habilitamos la feature para simular que estaba activa
                configDev.setEnabled(true);
                configProd.setEnabled(true);

                // Llamada al método de servicio
                featureService.disableFeatureForClientOrEnvironment(feature.getId(), requestDto);

                // Verificaciones
                assertFalse(configDev.getEnabled()); // La configuración objetivo debe estar deshabilitada
                assertTrue(configProd.getEnabled()); // La otra configuración no debe cambiar

                // Verificar que se haya guardado la entidad Feature
                verify(featureRepository, times(1)).save(feature);
        }

        @Test
        void testDisableFeatureForClientOrEnvironment_featureNotFound() {
                UUID randomId = UUID.randomUUID();
                when(featureRepository.findById(randomId)).thenReturn(Optional.empty());

                FeatureToggleRequestDto dto = FeatureToggleRequestDto.builder()
                                .clientId("clientA")
                                .build();

                assertThrows(FeatureFlagException.class,
                                () -> featureService.disableFeatureForClientOrEnvironment(randomId, dto));
        }

        @Test
        void testDisableFeatureForClientOrEnvironment_invalidRequest() {
                // Mock repository
                when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));

                // DTO inválido (ambos clientId y environment null)
                FeatureToggleRequestDto invalidDto = FeatureToggleRequestDto.builder()
                                .clientId(null)
                                .environment(null)
                                .build();

                assertThrows(FeatureFlagException.class,
                                () -> featureService.disableFeatureForClientOrEnvironment(feature.getId(), invalidDto));
        }

        @Test
        void validateFeatureToggleRequest_throwsException_whenClientIdAndEnvironmentAreNull() {
                when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));
                // Crear DTO con clientId y environment nulos
                FeatureToggleRequestDto invalidDto = FeatureToggleRequestDto.builder()
                                .clientId(null)
                                .environment(null)
                                .build();

                FeatureFlagException ex = assertThrows(FeatureFlagException.class, () -> {
                        featureService.disableFeatureForClientOrEnvironment(feature.getId(), invalidDto);
                });

                assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getMessage(), ex.getMessage());
                assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getDescription(), ex.getDescription());
                assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }

        @Test
        void validateFeatureToggleRequest_throwsException_whenClientIdAndEnvironmentAreEmpty() {
                when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));
                FeatureToggleRequestDto invalidDto = FeatureToggleRequestDto.builder()
                                .clientId(" ")
                                .environment(null)
                                .build();

                FeatureFlagException ex = assertThrows(FeatureFlagException.class, () -> {
                        featureService.disableFeatureForClientOrEnvironment(feature.getId(), invalidDto);
                });

                assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getMessage(), ex.getMessage());
                assertEquals(MessageError.FEATURE_TOGGLE_REQUEST_INVALID.getDescription(), ex.getDescription());
                assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }

}