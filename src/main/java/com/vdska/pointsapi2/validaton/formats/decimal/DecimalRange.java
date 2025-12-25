package com.vdska.pointsapi2.validaton.formats.decimal;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = DecimalRangeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecimalRange {
    String message() default "Value must be a number in range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String min();
    String max();

    boolean inclusiveMin() default true;
    boolean inclusiveMax() default true;

    boolean allowBlank() default true;
}
