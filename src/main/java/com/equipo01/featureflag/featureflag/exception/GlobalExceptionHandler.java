package com.equipo01.featureflag.featureflag.exception;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.equipo01.featureflag.featureflag.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;

/**
 * GlobalExceptionHandler gestiona de forma centralizada las excepciones lanzadas por los controladores REST.
 *
 * Utiliza {@link RestControllerAdvice} para interceptar y personalizar las respuestas de error en toda la API.
 * Cada método maneja un tipo específico de excepción y construye una respuesta estructurada usando {@link ErrorResponse}.
 *
 * Validaciones: Devuelve una lista de errores detallados si los datos de entrada no cumplen las restricciones.
 * Recursos: Informa si una feature ya existe o no se encuentra.
 * Errores genéricos: Captura cualquier excepción no gestionada y devuelve un error 500.
 *
 * Esto mejora la experiencia del cliente y facilita el debugging en desarrollo y producción.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de validación de argumentos en los endpoints REST.
     *
     * Cuando un DTO no cumple las restricciones de validación (por ejemplo, @NotNull, @Size),
     * este método recopila todos los errores.
     *
     * @param ex excepción lanzada por Spring al fallar la validación.
     * @return Lista de errores con detalles y mensajes específicos, devueltos en una lista de {@link ErrorResponse} con código 400 (BAD REQUEST).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation Error")
                .description(error.getDefaultMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
            errorResponses.add(errorResponse);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }

    /**
     * Maneja cualquier excepción no gestionada específicamente por otros métodos.
     *
     * @param ex excepción genérica lanzada en cualquier parte de la aplicación.
     * @return Error informando que ocurrió un problema inesperado. Devuelve un {@link ErrorResponse} con código 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Internal Server Error")
                .description("An unexpected error occurred")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando se recibe un JSON malformado en la solicitud.
     *
     * @param ex excepción lanzada por Spring cuando el JSON no se puede leer.
     * @return Error informando que el cuerpo de la solicitud no es un JSON válido. Devuelve un {@link ErrorResponse} con código 400 (BAD REQUEST).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message("Malformed JSON Request")
            .description("The request body is not valid JSON.")
            .code(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando una validación de parámetro de método falla.
     *
     * @param ex excepción lanzada por el sistema de validación de Java.
     * @return Error informando que una restricción de validación fue violada. Devuelve un {@link ErrorResponse} con código 400 (BAD REQUEST).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .description("Constraint Violation")
            .description("Validation failed for one or more parameters.")
            .code(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando el usuario no tiene permisos para acceder a un recurso.
     *
     * @param ex excepción lanzada por el sistema de seguridad.
     * @return Error informando que el usuario no tiene permisos. Devuelve un {@link ErrorResponse} con código 403 (FORBIDDEN).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .description("Access Denied")
            .description("You do not have permission to access this resource.")
            .code(HttpStatus.FORBIDDEN.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando se usa un método HTTP no permitido en el endpoint.
     *
     * @param ex excepción lanzada por Spring cuando el método HTTP no es soportado.
     * @return Error informando que el método HTTP no está permitido. Devuelve un {@link ErrorResponse} con código 405 (METHOD NOT ALLOWED).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .description("Method Not Allowed")
            .description("HTTP method not supported for this endpoint.")
            .code(HttpStatus.METHOD_NOT_ALLOWED.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando las credenciales de autenticación son inválidas.
     *
     * @param ex excepción lanzada por Spring Security.
     * @return Error informando que las credenciales son incorrectas. Devuelve un {@link ErrorResponse} con código 401 (UNAUTHORIZED).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .description("Authentication Failed")
            .description("Login failed due to incorrect credentials.")
            .code(HttpStatus.UNAUTHORIZED.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /////////////////////////// FEATURES /////////////////////////////////////////

    /**
     * Maneja la excepción cuando se intenta crear una feature que ya existe.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que la feature ya existe. Devuelve un {@link ErrorResponse} con código 409 (CONFLICT) y detalles del error.
     */
    @ExceptionHandler(FeatureAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFeatureAlreadyExists(FeatureAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Feature Already Exists")
                .description(ex.getMessage())
                .code(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando no se encuentra una feature solicitada.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que la feature no existe. Devuelve un {@link ErrorResponse} con código 404 (NOT FOUND) y detalles del error.
     */
    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeatureNotFound(FeatureNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Feature Not Found")
                .description(ex.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /////////////////////////// USER ///////////////////////////

    /**
     * Maneja la excepción cuando un usuario ya existe.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que el usuario ya existe. Devuelve un {@link ErrorResponse} con código 409 (CONFLICT).
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message("User Already Exists")
            .description(ex.getMessage())
            .code(HttpStatus.CONFLICT.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando las credenciales de autenticación son inválidas.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que las credenciales son incorrectas. Devuelve un {@link ErrorResponse} con código 401 (UNAUTHORIZED).
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message("Authentication Failed")
            .description("Login failed due to incorrect credentials.")
            .code(HttpStatus.UNAUTHORIZED.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}