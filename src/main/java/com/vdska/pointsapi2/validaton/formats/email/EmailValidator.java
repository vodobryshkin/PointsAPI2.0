package com.vdska.pointsapi2.validaton.formats.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Валидатор для обработки почты, переданной пользователем
 */
@Component
public class EmailValidator implements ConstraintValidator<Email, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) return true;
        return name.matches("^\\w+@\\w+\\.\\w{2,}$");
    }
}