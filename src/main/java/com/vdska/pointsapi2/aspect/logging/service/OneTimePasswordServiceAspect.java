package com.vdska.pointsapi2.aspect.logging.service;

import com.vdska.pointsapi2.domain.redis.OneTimePassword;
import com.vdska.pointsapi2.dto.token.OTPRequest;
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
public class OneTimePasswordServiceAspect {

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IOneTimePasswordService.verifyOTP(..))")
    public void verifyOtpPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IOneTimePasswordService.generateOTP(..))")
    public void generateOtpPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IOneTimePasswordService.saveOTP(..))")
    public void saveOtpPointcutMethod(){}

    @Around("verifyOtpPointcutMethod()")
    public Object logVerifyOtp(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        OTPRequest otpRequest = ((OTPRequest) pjp.getArgs()[0]);

        log.debug("Началась попытка верификации OTP '{}' с challengeId='{}'.", otpRequest.getCode(), otpRequest.getChallengeId());

        try {
            result = pjp.proceed();
            VerifyResponse verifyResponse = (VerifyResponse) result;

            if (verifyResponse.isStatus()) {
                log.debug("OTP '{}' был верифицирован как корректный. Пользователь: {}.", otpRequest.getCode(), verifyResponse.getMessage());
            } else {
                log.debug("OTP '{}' не прошёл верификацию. Причина='{}'.", otpRequest.getCode(), verifyResponse.getMessage());
            }

            return result;
        } finally {
            log.debug("Метод верификации OTP завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("generateOtpPointcutMethod()")
    public Object logGenerateOtp(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        result = pjp.proceed();

        String username = ((String) pjp.getArgs()[0]);
        String challengeID = ((String) pjp.getArgs()[1]);

        OneTimePassword oneTimePassword = (OneTimePassword) result;

        log.debug("OTP '{}' (challengeId='{}') был создан для пользователя c username='{}'.", oneTimePassword.getCode(), challengeID, username);

        return result;
    }

    @Around("saveOtpPointcutMethod()")
    public Object logSaveOtp(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        result = pjp.proceed();

        OneTimePassword oneTimePassword = ((OneTimePassword) pjp.getArgs()[0]);

        log.debug("OTP '{}' был сохранён в Redis.", oneTimePassword.toString());

        return result;
    }
}
