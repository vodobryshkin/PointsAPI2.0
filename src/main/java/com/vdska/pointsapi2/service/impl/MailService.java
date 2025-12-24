package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import com.vdska.pointsapi2.service.spec.IMailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

/**
 * Сервис для отправки писем электронной почты.
 */
@Service
@RequiredArgsConstructor
public class MailService implements IMailService {
    @Value("${confirmationlink.prefix}")
    private String prefix;

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * Метод для отправки письма для подтверждения аккаунта.
     *
     * @param confirmAccountMailRequest запрос на отправку письма.
     */
    public void sendConfirmationLetter(ConfirmAccountMailRequest confirmAccountMailRequest) {
        Context context = new Context();
        context.setVariable("username", confirmAccountMailRequest.getUsername());
        context.setVariable("confirmationLink", prefix + confirmAccountMailRequest.getLink());

        String htmlContent = templateEngine.process("confirmation", context);

        sendMessage(confirmAccountMailRequest.getTo(), confirmAccountMailRequest.getSubject(), htmlContent);
    }

//    /**
//     * Метод для отправки письма с OTP.
//     *
//     * @param otpMailRequest запрос на отправку письма.
//     */
//    @Async
//    public void sendOTPLetter(OTPMailRequest otpMailRequest) {
//        Context context = new Context();
//        context.setVariable("username", otpMailRequest.getUsername());
//        context.setVariable("date", otpMailRequest.getDate());
//        context.setVariable("userAgent", otpMailRequest.getUserAgent());
//        context.setVariable("otpCode", otpMailRequest.getCode());
//
//        String htmlContent = templateEngine.process("otp", context);
//
//        sendMessage(otpMailRequest.getTo(), otpMailRequest.getSubject(), htmlContent);
//    }

    private void sendMessage(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Не удалось отправить письмо", e);
        }
    }
}

