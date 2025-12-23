package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import com.vdska.pointsapi2.service.spec.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Метод для отправки запросов на отправку письма по электронной почте в очередь сообщений
 */
@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    private final AmqpTemplate template;

    @Value("${mail.queue.name}")
    private String queueName;

    /**
     * Метод для отправки сообщений с данными сервиса отправки почты на RabbitMQ-listener
     *
     * @param confirmAccountMailRequest запрос на отправку письма для подтверждения аккаунта по электронной почте.
     */
    @Override
    public void sendConfirmAccountMessageToQueue(ConfirmAccountMailRequest confirmAccountMailRequest) {
        template.convertAndSend(queueName, confirmAccountMailRequest);
    }
}
