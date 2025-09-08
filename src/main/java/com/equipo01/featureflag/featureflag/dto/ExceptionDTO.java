package com.equipo01.featureflag.featureflag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ExceptionDTO es una clase que se utiliza para transportar datos de excepciones
 * entre diferentes capas de una aplicación.
 * Su objetivo principal es encapsular y transferir información sobre errores de manera estructurada,
 * sin exponer la lógica interna ni las entidades del modelo de datos.
 * 
 * {@link Data} Anotación de Lombok que genera automáticamente los métodos getter, setter, toString, equals y hashCode.}
 * {@link AllArgsConstructor} Anotación de Lombok que genera un constructor con todos los campos como parámetros.
 * {@link NoArgsConstructor} Anotación de Lombok que genera un constructor sin parámetros
 * {@link Builder} Anotación de Lombok que permite crear instancias de la clase utilizando el patrón Builder.
 * {@link Schema} Anotación de Swagger que proporciona información sobre la API.
 * 
 * Atributos
 * - title: Título de la excepción.
 * - detail: Detalle de la excepción.
 * - status: Código de estado HTTP asociado a la excepción.
 * - reasons: Razones específicas de la excepción.
 * - timestamp: Marca de tiempo de cuando ocurrió la excepción.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO para transportar información de excepciones en la API.")
public class ExceptionDTO {
    @Schema(description = "Título de la excepción", example = "Unauthorized")
    private String title;

    @Schema(description = "Detalle de la excepción", example = "No tienes permisos para acceder a este recurso.")
    private String detail;

    @Schema(description = "Código de estado HTTP asociado a la excepción", example = "401")
    private int status;

    @Schema(description = "Razones específicas de la excepción (clave-valor)", example = "{ 'field': 'El campo es obligatorio' }")
    private Map<String, String> reasons;

    @Schema(description = "Marca de tiempo de cuando ocurrió la excepción", example = "2025-09-08T12:34:56")
    private LocalDateTime timestamp;
}