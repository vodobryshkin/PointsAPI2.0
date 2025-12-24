package com.vdska.pointsapi2.dto.token;

import lombok.Value;

/**
 * DTO, которое отправляется после обработки токена для подтверждения аккаунта сервисом для работы с токенами.
 */
@Value
public class VerifyResponse {
    boolean status;
    String username;
}
