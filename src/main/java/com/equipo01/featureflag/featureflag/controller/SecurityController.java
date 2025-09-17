package com.equipo01.featureflag.featureflag.controller;

import org.springframework.http.ResponseEntity;

public interface SecurityController {
     /**
   * Tests permission validation for feature configuration operations.
   *
   * @return a message indicating the permission test result and user access level
   */
  ResponseEntity<String> checkPermissionTest();
}
