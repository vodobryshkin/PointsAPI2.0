package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.domain.redis.OneTimePassword;
import com.vdska.pointsapi2.dto.token.OTPRequest;
import com.vdska.pointsapi2.dto.token.VerifyResponse;

/**
 * Интерфейс для определения функциональности OneTimePasswordService
 */
public interface IOneTimePasswordService {
    /**
     * Метод для подтверждения корректности OTP.
     *
     * @param otpRequest запрос на подтверждение.
     */
    VerifyResponse verifyOTP(OTPRequest otpRequest);

    /**
     * Метод для генерации одноразового пароля.
     *
     * @param username имя пользователя, с аккаунтом которого будет связан одноразовый пароль.
     * @return сгенерированную ссылку.
     */
    OneTimePassword generateOTP(String username, String challengeID);

    /**
     * Метод для сохранения в Redis OTP.
     *
     * @param oneTimePassword OTP.
     */
    void saveOTP(OneTimePassword oneTimePassword);
}
