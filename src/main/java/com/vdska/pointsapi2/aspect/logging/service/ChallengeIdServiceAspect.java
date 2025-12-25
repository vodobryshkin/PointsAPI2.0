package com.vdska.pointsapi2.aspect.logging.service;

import com.vdska.pointsapi2.domain.redis.ChallengeId;
import com.vdska.pointsapi2.dto.token.VerifyResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ChallengeIdServiceAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IChallengeIdService.verifyChallengeId(..))")
    public void verifyChallengeIdPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IChallengeIdService.generateChallengeId(..))")
    public void generateChallengeIdPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IChallengeIdService.saveChallengeId(..))")
    public void saveChallengeIdPointcutMethod(){}

    @Around("verifyChallengeIdPointcutMethod()")
    public Object logVerifyChallengeId(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String username = ((String) pjp.getArgs()[0]);
        String challengeID = ((String) pjp.getArgs()[1]);

        log.debug("Началась попытка верификации токена-подтверждение OTP '{}' для пользователя с username='{}'", challengeID, username);

        try {
            result = pjp.proceed();
            VerifyResponse verifyResponse = (VerifyResponse) result;

            if (verifyResponse.isStatus()) {
                log.debug("Токен-подтверждение с id='{}' был верифицирован как корректный. Пользователь: {}.", challengeID, username);
            } else {
                log.debug("Ссылка с id='{}' не прошла верификацию.", challengeID);
            }

            return result;
        } finally {
            log.debug("Метод верификации токена-подтверждения OTP был завершен. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("generateChallengeIdPointcutMethod()")
    public Object logGenerateChallengeId(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        result = pjp.proceed();

        String username = ((String) pjp.getArgs()[0]);
        String challengeID = ((String) pjp.getArgs()[1]);

        log.debug("Токен на подтверждение OTP '{}' был создан для пользователя c username='{}'.", challengeID, username);

        return result;
    }

    @Around("saveChallengeIdPointcutMethod()")
    public Object logSaveChallengeId(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        result = pjp.proceed();

        ChallengeId challengeID = ((ChallengeId) pjp.getArgs()[0]);

        log.debug("Токен на подтверждение OTP '{}' был сохранён в Redis.", challengeID.toString());

        return result;
    }
}
