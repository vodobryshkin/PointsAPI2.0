package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;

/**
 * Интерфейс для определения функциональности MailService
 */
public interface IMailService {
    /**
     * Метод для отправки письма для подтверждения аккаунта.
     *
     * @param confirmAccountMailRequest DTO с данными на отправку письма.
     */
    void sendConfirmationLetter(ConfirmAccountMailRequest confirmAccountMailRequest);
}
