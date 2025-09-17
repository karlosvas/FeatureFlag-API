package com.equipo01.featureflag.featureflag.anotations;

import com.equipo01.featureflag.featureflag.util.CompromisedPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation that ensures a password has not been compromised in known data
 * breaches. This annotation validates that the provided password is not present in databases of
 * compromised passwords, helping to enforce stronger security practices by preventing users from
 * using passwords that have been exposed in security breaches.
 *
 * <p>The validation is performed by the {@link CompromisedPasswordValidator} class, which
 * implements the actual logic for checking against compromised password databases or services.
 *
 * <p>This annotation can be applied to any field that represents a password to ensure it meets
 * security standards for password compromise detection.
 *
 * <p>Usage example:
 *
 * <pre>
 * public class UserRegistrationDTO {
 *     {@code @NotCompromised}
 *     {@code @NotBlank}
 *     private String password;
 *
 *     getters and setters
 * }
 * </pre>
 *
 * <p>When validation fails, the default error message "The provided password is compromised and
 * cannot be used." will be returned, though this can be customized by providing a custom message.
 *
 * <p>Custom message example:
 *
 * <pre>
 * {@code @NotCompromised(message = "Please choose a different password that hasn't been compromised")}
 * private String password;
 * </pre>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompromisedPasswordValidator.class)
public @interface NotCompromised {
  /**
   * The error message to be returned when validation fails.
   *
   * @return the error message template, supports message interpolation
   */
  String message() default "The provided password is compromised and cannot be used.";

  /**
   * Allows specification of validation groups to which this constraint belongs. This is useful for
   * performing validation in different contexts or phases.
   *
   * @return the groups the constraint belongs to
   */
  Class<?>[] groups() default {};

  /**
   * Can be used by clients of the Bean Validation API to assign custom payload objects to a
   * constraint. This attribute is not used by the API itself.
   *
   * @return the payload associated to the constraint
   */
  Class<? extends Payload>[] payload() default {};
}
