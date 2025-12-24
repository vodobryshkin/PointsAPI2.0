package com.vdska.pointsapi2.dto.user;

import com.vdska.pointsapi2.validaton.formats.email.Email;
import com.vdska.pointsapi2.validaton.formats.password.Password;
import com.vdska.pointsapi2.validaton.formats.username.Username;
import lombok.Data;

/**
 * DTO для передачи данных на эндпойнт /auth/register
 */
@Data
public class RegisterRequest {
    @Username
    private String username;
    @Email
    private String email;
    @Password
    private String password;
}
