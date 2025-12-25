package com.vdska.pointsapi2.dto.hit;

import com.vdska.pointsapi2.validaton.formats.decimal.DecimalRange;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO, который отправляется на сервис проверки на попадание точки на координатную плоскость.
 */
@Data
@NoArgsConstructor
public class AddHitRequest {
    @DecimalRange(min = "-3", max = "5", inclusiveMin = false, inclusiveMax = false,
            message = "X must contain only numbers and be in range (-3; 5)")
    private String x;

    @DecimalRange(min = "-3", max = "5", inclusiveMin = false, inclusiveMax = false,
            message = "Y must contain only numbers and be in range (-3; 5)")
    private String y;

    @DecimalRange(min = "0", max = "5", inclusiveMin = false, inclusiveMax = false,
            message = "R must contain only numbers and be in range (0; 5)")
    private String r;

    private boolean full;
}
