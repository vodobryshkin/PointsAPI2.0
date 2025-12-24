package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import com.vdska.pointsapi2.dto.mail.OTPMailRequest;

/**
 * Интерфейс для определения функциональности MessageServiceAspect
 */
public interface IMessageService {
    /**
     * Метод для отправки сообщений на подтверждение аккаунта на RabbitMQ-listener
     */
    void sendConfirmAccountMessageToQueue(ConfirmAccountMailRequest confirmAccountMailRequest);

    /**
     * Метод для отправки сообщений с OTP на RabbitMQ-listener
     */
    void sendOTPMessageToQueue(OTPMailRequest otpMailRequest);
}
