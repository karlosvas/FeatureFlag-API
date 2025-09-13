package com.equipo01.featureflag.featureflag.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.equipo01.featureflag.featureflag.dto.ErrorDto;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;

/**
 * GlobalExceptionHandler gestiona de forma centralizada las excepciones
 * lanzadas por los controladores REST.
 *
 * Utiliza {@link RestControllerAdvice} para interceptar y personalizar las
 * respuestas de error en toda la API.
 * Cada método maneja un tipo específico de excepción y construye una respuesta
 * estructurada usando {@link ErrorDto}.
 *
 * Validaciones: Devuelve una lista de errores detallados si los datos de
 * entrada no cumplen las restricciones.
 * Recursos: Informa si una feature ya existe o no se encuentra.
 * Errores genéricos: Captura cualquier excepción no gestionada y devuelve un
 * error 500.
 *
 * Esto mejora la experiencia del cliente y facilita el debugging en desarrollo
 * y producción.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja cualquier excepción no gestionada específicamente por otros métodos.
     *
     * @param ex excepción genérica lanzada en cualquier parte de la aplicación.
     * @return Error informando que ocurrió un problema inesperado. Devuelve un
     *         {@link ErrorDto} con código 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
            if (ex instanceof AccessDeniedException || ex instanceof AuthenticationException) {
        throw (RuntimeException) ex; // deja que lo maneje Spring Security
    }
        ErrorDto errorResponse = ErrorDto.builder()
                .message(MessageError.INTERNAL_SERVER_ERROR.getMessage())
                .description(ex.getMessage())
                .code(MessageError.INTERNAL_SERVER_ERROR.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(MessageError.INTERNAL_SERVER_ERROR.getStatus()).body(errorResponse);
    }

    /**
     * Maneja excepciones de acceso a datos a la base de datos.
     *
     * @param ex excepción lanzada cuando se interactua con la base de datos.
     * @return Error informando que ocurrió un problema en base de datos. Devuelve
     *         un {@link ErrorDto} con código 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDto> handleDataAccessException(DataAccessException ex) {
        ErrorDto errorResponse = ErrorDto.builder()
                .message(MessageError.DATA_ACCESS_ERROR.getMessage())
                .description(ex.getMessage())
                .code(MessageError.DATA_ACCESS_ERROR.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(MessageError.DATA_ACCESS_ERROR.getStatus()).body(errorResponse);
    }

    /**
     * Maneja excepciones de validación de argumentos en los endpoints REST.
     *
     * Cuando un DTO no cumple las restricciones de validación (por
     * ejemplo, @NotNull, @Size),
     * este método recopila todos los errores.
     *
     * @param ex excepción lanzada por Spring al fallar la validación.
     * @return Lista de errores con detalles y mensajes específicos, devueltos en
     *         una lista de {@link ErrorDto} con código 400 (BAD REQUEST).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDto>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorDto> errorResponses = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            ErrorDto errorResponse = ErrorDto.builder()
                    .message(MessageError.DTO_FIELDS_NOT_VALID.getMessage())
                    .description(error.getDefaultMessage())
                    .code(MessageError.DTO_FIELDS_NOT_VALID.getStatus().value())
                    .timestamp(LocalDateTime.now())
                    .build();
            errorResponses.add(errorResponse);
        });
        return ResponseEntity.status(MessageError.DTO_FIELDS_NOT_VALID.getStatus()).body(errorResponses);
    }

    /**
     * Maneja la excepción cuando se recibe un JSON malformado en la solicitud.
     *
     * @param ex excepción lanzada por Spring cuando el JSON no se puede leer.
     * @return Error informando que el cuerpo de la solicitud no es un JSON válido.
     *         Devuelve un {@link ErrorDto} con código 400 (BAD REQUEST).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleJsonParseError(HttpMessageNotReadableException ex) {
        ErrorDto errorResponse = ErrorDto.builder()
                .message(MessageError.MALFORMED_JSON.getMessage())
                .description(ex.getMessage())
                .code(MessageError.MALFORMED_JSON.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(MessageError.MALFORMED_JSON.getStatus()).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando una validación de parámetro de método falla.
     *
     * @param ex excepción lanzada por el sistema de validación de Java.
     * @return Error informando que una restricción de validación fue violada.
     *         Devuelve un {@link ErrorDto} con código 400 (BAD REQUEST).
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<List<ErrorDto>> handleMethodValidation(HandlerMethodValidationException ex) {
        List<ErrorDto> errorResponses = new ArrayList<>();
        ex.getAllErrors().forEach((error) -> {
            ErrorDto errorResponse = ErrorDto.builder()
                    .message(MessageError.VALIDATION_PARAMETER_NOT_VALID.getMessage())
                    .description(error.getDefaultMessage())
                    .code(MessageError.VALIDATION_PARAMETER_NOT_VALID.getStatus().value())
                    .timestamp(LocalDateTime.now())
                    .build();
            errorResponses.add(errorResponse);
        });
        return ResponseEntity.status(MessageError.VALIDATION_PARAMETER_NOT_VALID.getStatus()).body(errorResponses);
    }

    /**
     * Maneja la excepción cuando se usa un método HTTP no permitido en el endpoint.
     *
     * @param ex excepción lanzada por Spring cuando el método HTTP no es soportado.
     * @return Error informando que el método HTTP no está permitido. Devuelve un
     *         {@link ErrorDto} con código 405 (METHOD NOT ALLOWED).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErrorDto errorResponse = ErrorDto.builder()
                .message(MessageError.METHOD_NOT_ALLOWED.getMessage())
                .description(ex.getMessage())
                .code(MessageError.METHOD_NOT_ALLOWED.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(MessageError.METHOD_NOT_ALLOWED.getStatus()).body(errorResponse);
    }


    /////////////////////////// CUSTOM EXCEPTION ///////////////////////////

    /**
     * Maneja la excepción personalizada.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando validaciones de lógica de negocio. Devuelve un
     *         {@link ErrorDto}.
     */
    @ExceptionHandler(FeatureFlagException.class)
    public ResponseEntity<ErrorDto> handleFeatureFlagException(FeatureFlagException ex) {
        if (HttpStatus.NO_CONTENT.equals(ex.getStatus())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        ErrorDto errorResponse = ErrorDto.builder()
                .message(ex.getMessage())
                .description(ex.getDescription())
                .code(ex.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}