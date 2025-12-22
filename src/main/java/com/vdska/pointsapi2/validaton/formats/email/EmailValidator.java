package com.vdska.pointsapi2.validaton.formats.email;

import com.vdska.pointsapi2.validaton.formats.email.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Валидатор для обработки пароля, переданного пользователем
 */
@Component
public class EmailValidator implements ConstraintValidator<Email, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) return true;
        return name.matches("^\\w{8,32}$");
    }
}