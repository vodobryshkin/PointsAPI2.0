package com.vdska.pointsapi2.dto.auth;

import lombok.Value;

/**
 * DTO, которое возвращается при ошибке.
 */
@Value
public class AuthErrorResponse {
    boolean status;
    String message;
}
