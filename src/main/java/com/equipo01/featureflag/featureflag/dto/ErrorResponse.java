package com.equipo01.featureflag.featureflag.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExceptionDTO is a Data Transfer Object (DTO) designed to encapsulate information about exceptions
 * between different layers of an application.
 * Its main objective is to encapsulate and transfer information about errors in a structured way,
 * without exposing internal logic or data model entities.
 *
 * {@link Data} Lombok annotation that automatically generates getter, setter, toString, equals, and hashCode methods.
 * {@link AllArgsConstructor} Lombok annotation that generates a constructor with all fields as parameters.
 * {@link NoArgsConstructor} Lombok annotation that generates a no-arguments constructor.
 * {@link Builder} Lombok annotation that allows creating instances of the class using the Builder pattern.
 * {@link Schema} Swagger annotation that provides information about the API.
 *
 * Attributes:
 * - title: Title of the exception.
 * - detail: Details of the exception.
 * - status: HTTP status code associated with the exception.
 * - reasons: Specific reasons for the exception.
 * - timestamp: Timestamp of when the exception occurred.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for error responses")
public class ErrorResponse {
    @Schema(description = "Title of the exception", example = "Unauthorized")
    private String message;

    @Schema(description = "Details of the exception", example = "You do not have permission to access this resource.")
    private String description;

    @Schema(description = "HTTP status code associated with the exception", example = "401")
    private int code;

    @Schema(description = "Timestamp of when the exception occurred", example = "2025-09-08T12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}