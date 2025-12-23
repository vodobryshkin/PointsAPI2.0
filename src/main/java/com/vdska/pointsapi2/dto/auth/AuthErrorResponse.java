package com.vdska.pointsapi2.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;
import java.util.Map;

/**
 * DTO, которое возвращается при ошибке.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Value
public class AuthErrorResponse {
    boolean status;
    String message;
    @JsonProperty("validation_errors")
    Map<String, List<String>> validationErrors;
}
