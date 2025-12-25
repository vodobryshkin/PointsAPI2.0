package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.domain.redis.ChallengeId;
import com.vdska.pointsapi2.dto.token.VerifyResponse;

/**
 * Интерфейс для определения функциональности ChallengeIdService
 */
public interface IChallengeIdService {
    /**
     * Метод для подтверждения корректности ссылки на подтверждение аккаунта
     */
    VerifyResponse verifyChallengeId(String username, String challengeId);

    /**
     * Метод для генерации ссылки на подтверждение аккаунта
     *
     * @param username имя пользователя, с аккаунтом которого будет связана ссылка
     * @return сгенерированную ссылку.
     */
    ChallengeId generateChallengeId(String username, String challengeID);

    /**
     * Метод для сохранения в Redis токена на подтверждение аккаунта
     *
     * @param challengeId токен для подтверждения регистрации
     */
    void saveChallengeId(ChallengeId challengeId);
}
