package com.equipo01.featureflag.featureflag.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;

import jakarta.validation.ConstraintValidatorContext;

class CompromisedPasswordValidatorTest {

    @Mock
    private CompromisedPasswordChecker compromisedPasswordChecker;

    private CompromisedPasswordValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new CompromisedPasswordValidator(compromisedPasswordChecker);
    }

    @Test
    void testShouldReturnTrue_WhenPasswordIsNotCompromised() {
        String password = "SafePassword123!";
        CompromisedPasswordDecision decision = new CompromisedPasswordDecision(false);
        when(compromisedPasswordChecker.check(password)).thenReturn(decision);

        boolean result = validator.isValid(password, mockConstraintValidatorContext());

        assertTrue(result);
    }

    @Test
    void testShouldReturnFalse_WhenPasswordIsCompromised() {
        String password = "123456";
        CompromisedPasswordDecision decision = new CompromisedPasswordDecision(true);
        when(compromisedPasswordChecker.check(password)).thenReturn(decision);

        boolean result = validator.isValid(password, mockConstraintValidatorContext());

        assertFalse(result);
    }

    private ConstraintValidatorContext mockConstraintValidatorContext() {
        return mock(ConstraintValidatorContext.class);
    }
}
