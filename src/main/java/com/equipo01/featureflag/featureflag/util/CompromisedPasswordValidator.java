package com.equipo01.featureflag.featureflag.util;

import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;

import com.equipo01.featureflag.featureflag.anotations.NotCompromised;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * Custom constraint validator that checks if a password has been compromised in known data breaches.
 * 
 * This validator integrates with Spring Security's CompromisedPasswordChecker to validate
 * passwords against databases of known compromised passwords from security breaches.
 * It implements the Jakarta Bean Validation framework to provide declarative password
 * security validation through the {@link NotCompromised} annotation.
 * 
 * The validator works by:
 * 1. Receiving a password string to validate
 * 2. Using Spring Security's CompromisedPasswordChecker to check the password
 * 3. Returning false if the password is found in compromise databases
 * 4. Returning true if the password appears to be secure
 * 
 * This validation helps prevent users from choosing passwords that are known to be
 * compromised, thereby improving overall system security.
 * 
 */
@RequiredArgsConstructor
public class CompromisedPasswordValidator implements ConstraintValidator<NotCompromised, String> {
    
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    /**
     * Validates that the provided password has not been compromised in known data breaches.
     * 
     * @param password The password string to validate against compromise databases.
     *                 May be null (which will be considered valid by this validator).
     * @param context The constraint validator context provided by the validation framework.
     *                Used for adding custom error messages if needed.
     * @return true if the password is not found in compromise databases (valid),
     *         false if the password is found to be compromised (invalid)
     * 
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(password);
        return !decision.isCompromised();
    }
}
