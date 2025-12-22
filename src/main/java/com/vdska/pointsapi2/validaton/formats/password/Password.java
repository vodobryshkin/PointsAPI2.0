package com.vdska.pointsapi2.validaton.formats.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей, которые должны быть провалидированы валидатором PasswordValidator
 */
@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Password must be from 8 to 32 symbols (latin letters and '_')";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}