package com.vdska.pointsapi2.dto.hit;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

/**
 * DTO для описания полученных данных от сервиса для проверки попадания точек на координатную плоскость.
 */
@Value
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AddHitResponse {
    boolean status;
    String message;
    HitDTO hitDTO;
}
