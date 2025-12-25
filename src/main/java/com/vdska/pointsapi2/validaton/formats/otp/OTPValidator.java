package com.vdska.pointsapi2.validaton.formats.otp;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Валидатор для обработки имени пользователя
 */
@Component
public class OTPValidator implements ConstraintValidator<OTP, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) return true;
        return name.matches("^\\d{6}$");
    }
}