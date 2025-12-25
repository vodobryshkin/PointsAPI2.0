package com.vdska.pointsapi2.dto.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vdska.pointsapi2.validaton.formats.otp.OTP;
import com.vdska.pointsapi2.validaton.formats.uuid.UUID;
import lombok.Data;

/**
 * DTO для передачи в сервис подтверждения OTP кода.
 */
@Data
public class OTPRequest {
    @OTP
    private String code;

    @UUID
    @JsonProperty("challenge_id")
    private String challengeId;
}
