package com.equipo01.featureflag.featureflag.model.enums;

/*
 * Represents the different environments where a feature may be active.
 *
 * DEV: Development environment, where testing is performed and new features are implemented.
 * STAGING: Pre-production environment, where final testing is performed before production.
 * PROD: Production environment, where the feature is available to end users.
 */
public enum Environment {
  DEV,
  STAGING,
  PROD
}
