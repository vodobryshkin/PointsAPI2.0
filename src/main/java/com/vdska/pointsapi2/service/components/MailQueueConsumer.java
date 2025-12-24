package com.vdska.pointsapi2.service.components;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import com.vdska.pointsapi2.dto.mail.OTPMailRequest;
import com.vdska.pointsapi2.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailQueueConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "${mail.confirm.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void onConfirmAccount(ConfirmAccountMailRequest request) {
        mailService.sendConfirmationLetter(request);
    }

    @RabbitListener(queues = "${mail.otp.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void onOTP(OTPMailRequest otpMailRequest) {
        mailService.sendOTPLetter(otpMailRequest);
    }
}

