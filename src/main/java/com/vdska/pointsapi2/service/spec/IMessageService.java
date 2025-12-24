package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;

/**
 * Интерфейс для определения функциональности MessageServiceAspect
 */
public interface IMessageService {
    /**
     * Метод для отправки сообщений на RabbitMQ-listener
     */
    void sendConfirmAccountMessageToQueue(ConfirmAccountMailRequest confirmAccountMailRequest);
}
