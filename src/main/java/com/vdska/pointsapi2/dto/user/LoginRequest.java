package com.vdska.pointsapi2.dto.user;

import com.vdska.pointsapi2.validaton.formats.username.Username;
import lombok.Data;

/**
 * DTO для передачи данных на эндпойнт /auth/login
 */
@Data
public class LoginRequest {
    @Username
    private String username;
    private String password;
}
