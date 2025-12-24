package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.domain.redis.ConfirmationLink;
import com.vdska.pointsapi2.dto.confirm.VerifyResponse;
import com.vdska.pointsapi2.repository.IConfirmationLinkRepository;
import com.vdska.pointsapi2.service.spec.IConfirmationLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с кодами для подтверждения регистрации.
 */
@RequiredArgsConstructor
@Service
public class ConfirmationLinkService implements IConfirmationLinkService {
    @Value("${confirmationlink.ttl}")
    private Long ttl;

    private final IConfirmationLinkRepository confirmationLinkRepository;
    private static final VerifyResponse INVALID_TOKEN = new VerifyResponse(false, null);


    /**
     * Метод для подтверждения корректности ссылки на подтверждение аккаунта
     *
     * @param id id ссылки
     */
    @Override
    public VerifyResponse verifyConfirmationLink(String id) {
        UUID uuid = UUID.fromString(id);
        Optional<ConfirmationLink> confirmationLinkOptional = confirmationLinkRepository.findById(uuid);

        if (confirmationLinkOptional.isPresent()) {
            String username = confirmationLinkOptional.get().getUsername();
            confirmationLinkRepository.removeConfirmationLinkById(uuid);

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
    public ConfirmationLink generateConfirmationLink(String username) {
        return new ConfirmationLink(UUID.randomUUID(), username, ttl);
    }

    /**
     * Метод для сохранения в Redis токена на подтверждение аккаунта
     *
     * @param confirmationLink токен для подтверждения регистрации
     */
    @Override
    public void saveConfirmationLink(ConfirmationLink confirmationLink) {
        confirmationLinkRepository.save(confirmationLink);
    }
}
