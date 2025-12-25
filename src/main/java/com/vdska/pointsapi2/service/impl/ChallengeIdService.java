package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.domain.redis.ChallengeId;
import com.vdska.pointsapi2.dto.token.VerifyResponse;
import com.vdska.pointsapi2.repository.IChallengeIdRepository;
import com.vdska.pointsapi2.service.spec.IChallengeIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeIdService implements IChallengeIdService {
    @Value("${chid.ttl}")
    private long ttl;

    private final IChallengeIdRepository challengeIdRepository;

    private static final VerifyResponse INVALID_TOKEN = new VerifyResponse(false, "CHALLENGE_ID_NOT_VALID");

    /**
     * Метод для подтверждения корректности ссылки на подтверждение аккаунта
     *
     * @param username имя пользователя которому принадлежит username
     */
    @Override
    public VerifyResponse verifyChallengeId(String username, String challengeId) {
        Optional<ChallengeId> challengeIdOptional = challengeIdRepository.findById(username);

        if (challengeIdOptional.isEmpty()) {
            return INVALID_TOKEN;
        }

        String id = challengeIdOptional.get().getId();

        if (id.equals(challengeId)) {
            challengeIdRepository.deleteById(id);

            return new VerifyResponse(true, username);
        }

        return INVALID_TOKEN;
    }

    /**
     * Метод для генерации ссылки на подтверждение аккаунта
     *
     * @param username имя пользователя, с аккаунтом которого будет связана ссылка
     * @return сгенерированную ссылку.
     */
    @Override
    public ChallengeId generateChallengeId(String username, String challengeID) {
        return new ChallengeId(challengeID, username, ttl);
    }

    /**
     * Метод для сохранения в Redis токена на подтверждение аккаунта
     *
     * @param challengeId токен для подтверждения регистрации
     */
    @Override
    public void saveChallengeId(ChallengeId challengeId) {
        challengeIdRepository.save(challengeId);
    }
}
