package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;

/**
 * Интерфейс для определения функциональности MessageService
 */
public interface IMessageService {
    /**
     * Метод для отправки сообщений на RabbitMQ-listener
     */
    void sendConfirmAccountMessageToQueue(ConfirmAccountMailRequest confirmAccountMailRequest);
}
