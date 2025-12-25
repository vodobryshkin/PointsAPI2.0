package com.vdska.pointsapi2.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class LoginResponse {
    boolean status;
    @JsonProperty("otp_required")
    boolean otpRequired;

    @JsonProperty("challenge_id")
    String challengeId;
}
