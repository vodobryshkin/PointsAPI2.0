package com.vdska.pointsapi2.validaton.formats.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей, которые должны быть провалидированы валидатором EmailValidator
 */
@Constraint(validatedBy = EmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    String message() default "Email must match regex \"^\\w+@\\w+\\.\\w{2}$\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}