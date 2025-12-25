package com.vdska.pointsapi2.dto.hit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HitDTO {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    private boolean status;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy.MM.dd'T'HH:mm:ss"
    )
    @JsonProperty("date_of_creation")
    private LocalDateTime dateTime;
}
