package com.vdska.pointsapi2.validaton.formats.otp;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей, которые должны быть провалидированы валидатором OTPValidator
 */
@Constraint(validatedBy = OTPValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OTP {
    String message() default "Username must be 6 symbols and contain only numbers";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
