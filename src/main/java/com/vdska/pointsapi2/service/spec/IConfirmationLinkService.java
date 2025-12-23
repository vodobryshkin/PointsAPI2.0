package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.domain.redis.ConfirmationLink;

/**
 * Интерфейс для определения функциональности ConfirmationLinkService
 */
public interface IConfirmationLinkService {
    /**
     * Метод для генерации ссылки на подтверждение аккаунта
     *
     * @param username имя пользователя, с аккаунтом которого будет связана ссылка
     * @return сгенерированную ссылку.
     */
    ConfirmationLink generateConfirmationLink(String username);

    /**
     * Метод для сохранения в Redis токена на подтверждение аккаунта
     *
     * @param confirmationLink токен для подтверждения регистрации
     */
    void saveConfirmationLink(ConfirmationLink confirmationLink);
}
