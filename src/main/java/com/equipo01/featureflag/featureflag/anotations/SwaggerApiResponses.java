package com.equipo01.featureflag.featureflag.anotations;

import com.equipo01.featureflag.featureflag.dto.ErrorDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation that provides a standardized set of common HTTP response definitions for
 * OpenAPI/Swagger documentation.
 *
 * <p>This annotation can be applied to REST controller classes or individual endpoint methods to
 * automatically include standard HTTP response codes and their descriptions in the generated API
 * documentation.
 *
 * <p>The annotation includes the following HTTP response definitions:
 *
 * <ul>
 *   <li><strong>2xx Success responses:</strong>
 *       <ul>
 *         <li>200 - Successful request
 *         <li>201 - Resource created successfully
 *         <li>204 - No content
 *       </ul>
 *   <li><strong>4xx Client error responses:</strong>
 *       <ul>
 *         <li>400 - Bad request
 *         <li>401 - Unauthorized
 *         <li>403 - Forbidden
 *         <li>404 - Resource not found
 *         <li>405 - Method not allowed
 *         <li>408 - Request timeout
 *         <li>409 - Conflict
 *         <li>422 - Unprocessable entity
 *       </ul>
 *   <li><strong>5xx Server error responses:</strong>
 *       <ul>
 *         <li>500 - Internal server error
 *         <li>503 - Service unavailable
 *       </ul>
 * </ul>
 *
 * <p>Error responses (4xx and 5xx) automatically include the {@link ErrorDto} schema for consistent
 * error response structure across the API.
 *
 * <h3>Usage Examples:</h3>
 *
 * <pre>
 * Apply to entire controller
 * {@code @SwaggerApiResponses}
 * {@code @RestController}
 * {@code @RequestMapping("/api/features")}
 * public class FeatureController {
 *     All methods inherit the standard response definitions
 * }
 *
 * Apply to specific endpoint
 * {@code @SwaggerApiResponses}
 * {@code @GetMapping("/{id}")}
 * public ResponseEntity&lt;FeatureDto&gt; getFeature(@PathVariable Long id) {
 *     Method-specific response definitions
 * }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "404",
          description = "Resource not found",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "405",
          description = "Method not allowed",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "408",
          description = "Request timeout",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "422",
          description = "Unprocessable entity",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "500",
          description = "Internal server error",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class))),
      @ApiResponse(
          responseCode = "503",
          description = "Service unavailable",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorDto.class)))
    })
public @interface SwaggerApiResponses {}
