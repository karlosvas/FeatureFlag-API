package com.equipo01.featureflag.featureflag.util;

import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;

import com.equipo01.featureflag.featureflag.anotations.NotCompromised;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompromisedPasswordValidator implements ConstraintValidator<NotCompromised, String> {
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(password);
        return !decision.isCompromised();
    }
}
