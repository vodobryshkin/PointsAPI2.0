package com.vdska.pointsapi2.aspect.logging.service;

import com.vdska.pointsapi2.domain.redis.ConfirmationLink;
import com.vdska.pointsapi2.dto.confirm.VerifyResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ConfirmationLinkAspectServiceAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IConfirmationLinkService.verifyConfirmationLink(..))")
    public void verifyConfirmationLinkPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IConfirmationLinkService.generateConfirmationLink(..))")
    public void generateConfirmationLinkPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IConfirmationLinkService.saveConfirmationLink(..))")
    public void saveConfirmationLinkLinkPointcutMethod(){}

    @Around("verifyConfirmationLinkPointcutMethod()")
    public Object logVerifyConfirmationLink(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String id = (String) pjp.getArgs()[0];

        log.debug("Началась попытка верификации ссылки на подтверждение аккаунта с id='{}'", id);

        try {
            result = pjp.proceed();
            VerifyResponse verifyResponse = (VerifyResponse) result;

            if (verifyResponse.isStatus()) {
                log.debug("Ссылка с id='{}' была верифицирована как корректная. Пользователь: {}.", id, verifyResponse.getUsername());
            } else {
                log.debug("Ссылка с id='{}' не прошла верификацию.", id);
            }

            return result;
        } finally {
            log.debug("Метод верификации ссылки на подтверждение аккаунта завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("generateConfirmationLinkPointcutMethod()")
    public Object logGenerateConfirmationLink(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        result = pjp.proceed();

        String username = ((String) pjp.getArgs()[0]);

        log.debug("Была создана ссылка на подтверждение аккаунта для пользователя c username='{}'.", username);

        return result;
    }

    @Around("saveConfirmationLinkLinkPointcutMethod()")
    public Object logSaveConfirmationLinkLink(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        result = pjp.proceed();

        String username = ((ConfirmationLink) pjp.getArgs()[0]).getUsername();

        log.debug("Ссылка на подтверждение аккаунта для пользователя с username='{}' была сохранена в хранилище.", username);

        return result;
    }
}
