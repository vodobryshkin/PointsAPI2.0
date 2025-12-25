package com.vdska.pointsapi2.dto.hit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;
import java.util.Map;

/**
 * DTO, которое возвращается при ошибке при действиях, связанных с работой с попаданиями.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
public class HitErrorResponse {
    boolean status;
    String message;
    @JsonProperty("validation_errors")
    Map<String, List<String>> validationErrors;
}
