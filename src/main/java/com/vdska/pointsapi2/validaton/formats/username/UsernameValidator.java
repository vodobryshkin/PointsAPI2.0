package com.vdska.pointsapi2.validaton.formats.username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Валидатор для обработки имени пользователя
 */
@Component
public class UsernameValidator implements ConstraintValidator<Username, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) return true;
        return name.matches("^\\w{6,32}$");
    }
}