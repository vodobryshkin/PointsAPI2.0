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
public class MessageServiceAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IMessageService.sendConfirmAccountMessageToQueue())")
    public void sendConfirmAccountMessageToQueuePointcutMethod(){}

    @Around("sendConfirmAccountMessageToQueuePointcutMethod()")
    public Object logSendConfirmAccountMessageToQueuePointcutMethod(ProceedingJoinPoint pjp) throws Throwable{
        Object result;
        result = pjp.proceed();
        ConfirmAccountMailRequest request = (ConfirmAccountMailRequest) pjp.getArgs()[0];

        String username = request.getUsername();
        String email = request.getTo();

        log.info("Данные для письма на подтверждение аккаунта для пользователя с username='{}' и email='{}' были отправлены в очередь сообщений.", username, email);
        return result;
    }
}
