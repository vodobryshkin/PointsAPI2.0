package com.vdska.pointsapi2.service.spec;


import com.vdska.pointsapi2.dto.user.RegisterRequest;

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

    /**
     * Метод для верификации нового пользователя в системе.
     *
     * @param username имя пользователя для верификации.
     */
    void verify(String username);

    /**
     * Метод для получения пользователь с email.
     *
     * @param username имя пользователя, почту которого нужно получить.
     * @return почта пользователя.
     */
    String getEmail(String username);
}
