package com.vdska.pointsapi2.validaton.formats.username;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей, которые должны быть провалидированы валидатором UsernameValidator
 */
@Constraint(validatedBy = UsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
    String message() default "Username must be from 6 to 32 symbols (latin letters and '_')";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}