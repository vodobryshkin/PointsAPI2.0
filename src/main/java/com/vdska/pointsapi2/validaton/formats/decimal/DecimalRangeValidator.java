package com.vdska.pointsapi2.validaton.formats.decimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DecimalRangeValidator implements ConstraintValidator<DecimalRange, String> {

    private BigDecimal min;
    private BigDecimal max;
    private boolean inclusiveMin;
    private boolean inclusiveMax;
    private boolean allowBlank;

    @Override
    public void initialize(DecimalRange constraint) {
        this.min = new BigDecimal(constraint.min());
        this.max = new BigDecimal(constraint.max());
        this.inclusiveMin = constraint.inclusiveMin();
        this.inclusiveMax = constraint.inclusiveMax();
        this.allowBlank = constraint.allowBlank();
    }

    @Override
    public boolean isValid(String coordinate, ConstraintValidatorContext context) {
        if (coordinate == null || coordinate.isBlank()) return allowBlank;

        String value = coordinate.trim().replace(',', '.');

        if (!value.matches("^[+-]?(\\d+(\\.\\d+)?|\\.\\d+)$")) return false;

        BigDecimal x;
        try {
            x = new BigDecimal(value);
        } catch (NumberFormatException ex) {
            return false;
        }

        int cMin = x.compareTo(min);
        int cMax = x.compareTo(max);

        boolean okMin = inclusiveMin ? cMin >= 0 : cMin > 0;
        boolean okMax = inclusiveMax ? cMax <= 0 : cMax < 0;

        return okMin && okMax;
    }
}
