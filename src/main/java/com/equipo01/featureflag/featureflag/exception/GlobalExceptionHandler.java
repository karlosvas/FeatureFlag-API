package com.equipo01.featureflag.featureflag.exception;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.equipo01.featureflag.featureflag.dto.ExceptionDTO;
import jakarta.validation.ConstraintViolationException;


/**
 * GlobalExceptionHandler gestiona de forma centralizada las excepciones lanzadas por los controladores REST.
 * 
 * Utiliza {@link RestControllerAdvice} para interceptar y personalizar las respuestas de error en toda la API.
 * Cada método maneja un tipo específico de excepción y construye una respuesta estructurada usando {@link ExceptionDTO}.
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
     * este método recopila todos los errores y los devuelve en una lista de {@link ExceptionDTO} con código 400.
     *
     * @param ex excepción lanzada por Spring al fallar la validación.
     * @return Lista de errores con detalles y mensajes específicos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionDTO>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ExceptionDTO> errorResponses = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            ExceptionDTO errorResponse = ExceptionDTO.builder()
                .title("Validation Error")
                .detail("Invalid input data")
                .status(HttpStatus.BAD_REQUEST.value())
                .reasons(Map.of("reason", errorMessage))
                .timestamp(LocalDateTime.now())
                .build();
            errorResponses.add(errorResponse);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }
   

    /**
     * Maneja cualquier excepción no gestionada específicamente por otros métodos.
     * 
     * Devuelve un {@link ExceptionDTO} con código 500 (INTERNAL SERVER ERROR) y detalles del error inesperado.
     *
     * @param ex excepción genérica lanzada en cualquier parte de la aplicación.
     * @return Error informando que ocurrió un problema inesperado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
                .title("Internal Server Error")
                .detail("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .reasons(Map.of("reason", "A server error occurred. Please contact support if the problem persists."))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando se recibe un JSON malformado en la solicitud.
     * 
     * Devuelve un {@link ExceptionDTO} con código 400 (BAD REQUEST) y detalles del error.
     *
     * @param ex excepción lanzada por Spring cuando el JSON no se puede leer.
     * @return Error informando que el cuerpo de la solicitud no es un JSON válido.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleJsonParseError(HttpMessageNotReadableException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
            .title("Malformed JSON Request")
            .detail("The request body is not valid JSON.")
            .status(HttpStatus.BAD_REQUEST.value())
            .reasons(Map.of("reason", ex.getMessage()))
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando una validación de parámetro de método falla.
     * 
     * Devuelve un {@link ExceptionDTO} con código 400 (BAD REQUEST) y detalles del error de restricción.
     *
     * @param ex excepción lanzada por el sistema de validación de Java.
     * @return Error informando que una restricción de validación fue violada.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleConstraintViolation(ConstraintViolationException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
            .title("Constraint Violation")
            .detail("Validation failed for one or more parameters.")
            .status(HttpStatus.BAD_REQUEST.value())
            .reasons(Map.of("violations", ex.getMessage()))
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando el usuario no tiene permisos para acceder a un recurso.
     * 
     * Devuelve un {@link ExceptionDTO} con código 403 (FORBIDDEN) y detalles del error de acceso denegado.
     *
     * @param ex excepción lanzada por el sistema de seguridad.
     * @return Error informando que el usuario no tiene permisos.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> handleAccessDenied(AccessDeniedException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
            .title("Access Denied")
            .detail("You do not have permission to access this resource.")
            .status(HttpStatus.FORBIDDEN.value())
            .reasons(Map.of("reason", ex.getMessage()))
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando se usa un método HTTP no permitido en el endpoint.
     * 
     * Devuelve un {@link ExceptionDTO} con código 405 (METHOD NOT ALLOWED) y detalles del error.
     *
     * @param ex excepción lanzada por Spring cuando el método HTTP no es soportado.
     * @return Error informando que el método HTTP no está permitido.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
            .title("Method Not Allowed")
            .detail("HTTP method not supported for this endpoint.")
            .status(HttpStatus.METHOD_NOT_ALLOWED.value())
            .reasons(Map.of("method", ex.getMethod()))
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

      /**
     * Maneja la excepción cuando las credenciales de autenticación son inválidas.
     *
     * @param ex excepción lanzada por Spring Security.
     * @return Error informando que las credenciales son incorrectas.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDTO> handleBadCredentials(BadCredentialsException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
            .title("Authentication Failed")
            .detail("Login failed due to incorrect credentials.")
            .status(HttpStatus.UNAUTHORIZED.value())
            .reasons(Map.of("reason", "Incorrect username or password."))
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /////////////////////////// FEATURES /////////////////////////////////////////
     /**
     * Maneja la excepción cuando se intenta crear una feature que ya existe.
     * 
     * Devuelve un {@link ExceptionDTO} con código 409 (CONFLICT) y detalles del error.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que la feature ya existe.
     */
    @ExceptionHandler(FeatureAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleFeatureAlreadyExists(FeatureAlreadyExistsException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
                .title("Feature Already Exists")
                .detail("The feature you are trying to create already exists.")
                .status(HttpStatus.CONFLICT.value())
               .reasons(Map.of())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando no se encuentra una feature solicitada.
     * 
     * Devuelve un {@link ExceptionDTO} con código 404 (NOT FOUND) y detalles del error.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que la feature no existe.
     */
    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleFeatureNotFound(FeatureNotFoundException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
                .title("Feature Not Found")
                .detail("The feature you are trying to access does not exist.")
                .status(HttpStatus.NOT_FOUND.value())
                .reasons(Map.of())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando un usuario ya existe.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que el usuario ya existe.
     */
    /////////////////////////// USER ///////////////////////////
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
            .title("User Already Exists")
            .detail(ex.getMessage())
            .status(HttpStatus.CONFLICT.value())
            .reasons(Map.of())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Maneja la excepción cuando las credenciales de autenticación son inválidas.
     *
     * @param ex excepción personalizada lanzada por la lógica de negocio.
     * @return Error informando que las credenciales son incorrectas.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidCredentials(InvalidCredentialsException ex) {
        ExceptionDTO errorResponse = ExceptionDTO.builder()
            .title("Authentication Failed")
            .detail("Login failed due to incorrect credentials.")
            .status(HttpStatus.UNAUTHORIZED.value())
            .reasons(Map.of())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}