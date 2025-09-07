package com.equipo01.featureflag.featureflag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

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
public class ExceptionDTO {
    private String title;
    private String detail;
    private int status;
    private Map<String, String> reasons;
    private LocalDateTime timestamp;
}