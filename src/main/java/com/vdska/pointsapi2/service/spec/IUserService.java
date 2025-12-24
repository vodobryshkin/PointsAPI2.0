package com.vdska.pointsapi2.service.spec;


import com.vdska.pointsapi2.dto.auth.RegisterRequest;

/**
 * Интерфейс для определения функциональности UserService
 */
public interface IUserService {
    /**
     * Метод для регистрации нового пользователя в системе.
     *
     * @param registerRequest запрос на регистрацию пользователя.
     */
    void register(RegisterRequest registerRequest);
}
