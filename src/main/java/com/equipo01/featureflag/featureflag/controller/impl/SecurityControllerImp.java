package com.equipo01.featureflag.featureflag.controller.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equipo01.featureflag.featureflag.anotations.SwaggerApiResponses;
import com.equipo01.featureflag.featureflag.controller.SecurityController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("${api.security}")
public class SecurityControllerImp implements SecurityController {
    
  /**
   * Test endpoint for verifying administrative permissions.
   *
   * @return ResponseEntity with a success message if the user has proper permissions
   * @throws AccessDeniedException if the user does not have ADMIN role
   */
  @GetMapping("/test")
  @PreAuthorize("hasRole('ADMIN')")
  @SwaggerApiResponses
  @ApiResponse(
      responseCode = "200",
      description = "Admin permission test successful",
      content =
          @Content(
              mediaType = "text/plain",
              schema = @Schema(type = "string", example = "Test permission ok")))
  @Operation(
      summary = "Test endpoint for verifying ADMIN permissions",
      description = "Accessible only by users with ADMIN role to confirm permission settings.")
  public ResponseEntity<String> checkPermissionTest() {
    return ResponseEntity.ok("Test permission ok");
  }
}
