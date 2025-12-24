package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.domain.redis.OneTimePassword;
import com.vdska.pointsapi2.dto.token.VerifyResponse;

/**
 * Интерфейс для определения функциональности OneTimePasswordService
 */
public interface IOneTimePasswordService {
    /**
     * Метод для подтверждения корректности OTP.
     *
     * @param code код.
     */
    VerifyResponse verifyOTP(String code);

    /**
     * Метод для генерации одноразового пароля.
     *
     * @param username имя пользователя, с аккаунтом которого будет связан одноразовый пароль.
     * @return сгенерированную ссылку.
     */
    OneTimePassword generateOTP(String username);

    /**
     * Метод для сохранения в Redis OTP.
     *
     * @param oneTimePassword OTP.
     */
    void saveOTP(OneTimePassword oneTimePassword);
}
