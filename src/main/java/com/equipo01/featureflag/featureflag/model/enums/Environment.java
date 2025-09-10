package com.equipo01.featureflag.featureflag.model.enums;
/*
 * Representa los diferentes entornos donde una funcionalidad puede estar activa
 *
 * DEV: Entorno de desarrollo, donde se realizan pruebas y se implementan nuevas características.
 * STAGING: Entorno de preproducción, donde se realizan pruebas finales antes de la producción.
 * PROD: Entorno de producción, donde la funcionalidad está disponible para los usuarios finales.
 */
public enum Environment {
    DEV,
    STAGING,
    PROD
}
