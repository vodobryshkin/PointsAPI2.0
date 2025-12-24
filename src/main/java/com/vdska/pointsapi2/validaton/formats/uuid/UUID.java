package com.vdska.pointsapi2.validaton.formats.uuid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей, которые должны быть провалидированы валидатором UUIDValidator
 */
@Constraint(validatedBy = UUIDValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UUID {
    String message() default "UUID does not match the format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}