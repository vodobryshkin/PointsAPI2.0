package com.vdska.pointsapi2.aspect.logging.service;

import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MailServiceAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IMailService.sendConfirmationLetter(..))")
    public void sendConfirmationLetterPointcutMethod(){}

    @Around("sendConfirmationLetterPointcutMethod()")
    public Object logSendConfirmationLetter(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        result = pjp.proceed();
        ConfirmAccountMailRequest request = (ConfirmAccountMailRequest) pjp.getArgs()[0];

        String username = request.getUsername();
        String email = request.getTo();

        log.debug("Письмо с ссылкой на подтверждение аккаунта для пользователя с username='{}' было отправлено на email {}.", username, email);
        return result;
    }

}
