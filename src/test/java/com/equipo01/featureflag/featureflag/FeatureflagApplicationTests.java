package com.equipo01.featureflag.featureflag;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Pruebas de integración para la aplicación Feature Flag.
 *
 * Esta clase verifica que el contexto de Spring Boot se cargue correctamente
 * utilizando el perfil "staging", el cual emplea una base de datos H2 en memoria
 * para facilitar pruebas aisladas y reproducibles.
 *
 * Anotaciones utilizadas:
 * - {@link SpringBootTest}: Carga el contexto completo de la aplicación para pruebas de integración.
 * - {@link ActiveProfiles}: Activa el perfil "staging" para configurar beans y propiedades específicas de pruebas.
 */
@SpringBootTest
@ActiveProfiles("staging")
class FeatureflagApplicationTests {

    /**
     * Verifica que el contexto de la aplicación se cargue sin errores.
     * Si el contexto no se carga correctamente, la prueba fallará.
     */
    @Test
    void contextLoads() {
    }
}
