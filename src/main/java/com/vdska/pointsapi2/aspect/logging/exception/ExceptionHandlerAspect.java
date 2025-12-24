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

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleConfirmationException(..))")
    public void handleConfirmationExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleInvalidCreditsOfConfirmationUserException(..))")
    public void handleInvalidCreditsOfConfirmationUserExceptionPointcutMethod(){}

    @AfterReturning(
            pointcut = "handleRegisterExceptionPointcutMethod()",
            returning = "response")
    public void logHandleRegisterExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        AuthErrorResponse authResponse = response.getBody();
        log.info("Эндпойнт /auth/register вернул ответ c ошибкой: status='{}', body='{}'.",
                Objects.requireNonNull(authResponse).isStatus(), authResponse);
    }

    @AfterReturning(
            pointcut = "handleValidationExceptionPointcutMethod()",
            returning = "response")
    public void logHandleValidationExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.info("Запрос не прошёл валидацию: body='{}'.", response.getBody());
    }

    @AfterReturning(
            pointcut = "handleConfirmationExceptionPointcutMethod(), handleConfirmationExceptionPointcutMethod()",
            returning = "response")
    public void logHandleConfirmationExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.info("Эндпойнт /auth/confirm вернул ответ c ошибкой: message='{}'.", response.getBody());
    }
}
