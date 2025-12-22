package com.vdska.pointsapi2.service.spec;


import com.vdska.pointsapi2.dto.auth.RegisterRequest;

/**
 * Интерфейс для определения функциональности AuthService
 */
public interface IAuthService {
    /**
     * Метод для регистрации нового пользователя в системе.
     *
     * @param registerRequest запрос на регистрацию пользователя.
     */
    void register(RegisterRequest registerRequest);
}
