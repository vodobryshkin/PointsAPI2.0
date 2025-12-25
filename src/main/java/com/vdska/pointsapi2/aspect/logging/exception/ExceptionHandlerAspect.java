package com.vdska.pointsapi2.aspect.logging.exception;

import com.vdska.pointsapi2.dto.user.AuthErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ExceptionHandlerAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleRegisterException(..))")
    public void handleRegisterExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleValidationException(..))")
    public void handleValidationExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleVerifyException(..))")
    public void handleVerifyExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleInvalidCreditsOfConfirmationUserException(..))")
    public void handleInvalidCreditsOfConfirmationUserExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleHandlerMethodValidationException(..))")
    public void handleHandlerMethodValidationExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleCreditsException(..))")
    public void handleCreditsExceptionPointcutMethod(){}

    @AfterReturning(
            pointcut = "handleRegisterExceptionPointcutMethod()",
            returning = "response")
    public void logHandleRegisterExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        AuthErrorResponse authResponse = response.getBody();
        log.info("Эндпойнт /auth/register вернул ответ c ошибкой: status='{}', body='{}'.",
                Objects.requireNonNull(authResponse).isStatus(), authResponse);
    }

    @AfterReturning(
            pointcut = "handleValidationExceptionPointcutMethod() || handleHandlerMethodValidationExceptionPointcutMethod()",
            returning = "response")
    public void logHandleValidationExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.debug("Запрос не прошёл валидацию: body='{}'.", response.getBody());
    }

    @AfterReturning(
            pointcut = "handleVerifyExceptionPointcutMethod()",
            returning = "response")
    public void logHandleConfirmationExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        switch (Objects.requireNonNull(response.getBody()).getMessage()) {
            case "OTP_NOT_VALID", "CHID_NOT_VALID" -> log.info("Эндпойнт на POST /auth/otp вернул ответ c ошибкой: message='{}'.", response.getBody());
            default -> log.info("Эндпойнт на GET /auth/confirm вернул ответ c ошибкой: message='{}'.", response.getBody());
        }
    }

    @AfterReturning(
            pointcut = "handleCreditsExceptionPointcutMethod()",
            returning = "response")
    public void logHandleCreditsExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.info("Эндпойнт на POST /auth/confirm вернул ответ c ошибкой: message='{}'.", response.getBody());
    }
}
