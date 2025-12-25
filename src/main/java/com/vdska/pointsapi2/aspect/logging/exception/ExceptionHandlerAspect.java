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

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleHandlerMethodValidationException(..))")
    public void handleHandlerMethodValidationExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleVerifyException(..))")
    public void handleVerifyExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleInvalidCreditsOfConfirmationUserException(..))")
    public void handleInvalidCreditsOfConfirmationUserExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleCreditsException(..))")
    public void handleCreditsExceptionPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleBadJson(..))")
    public void handleBadJsonPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleIllegalArgument(..))")
    public void handleIllegalArgumentPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleRuntime(..))")
    public void handleRuntimePointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleOther(..))")
    public void handleOtherPointcutMethod(){}

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
    public void logHandleVerifyExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        String msg = Objects.requireNonNull(response.getBody()).getMessage();
        switch (msg) {
            case "OTP_NOT_VALID", "CHALLENGE_ID_NOT_VALID" -> log.info("Эндпойнт POST /auth/otp вернул ответ c ошибкой: body='{}'.", response.getBody());
            default -> log.info("Эндпойнт GET /auth/confirm вернул ответ c ошибкой: body='{}'.", response.getBody());
        }
    }

    @AfterReturning(
            pointcut = "handleInvalidCreditsOfConfirmationUserExceptionPointcutMethod()",
            returning = "response")
    public void logHandleInvalidCreditsOfConfirmationUserExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.info("Ошибка подтверждения/учётных данных: body='{}'.", response.getBody());
    }

    @AfterReturning(
            pointcut = "handleCreditsExceptionPointcutMethod()",
            returning = "response")
    public void logHandleCreditsExceptionAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.info("Ошибка учётных данных: body='{}'.", response.getBody());
    }

    @AfterReturning(
            pointcut = "handleBadJsonPointcutMethod()",
            returning = "response")
    public void logHandleBadJsonAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.debug("Некорректный JSON в запросе: body='{}'.", response.getBody());
    }

    @AfterReturning(
            pointcut = "handleIllegalArgumentPointcutMethod()",
            returning = "response")
    public void logHandleIllegalArgumentAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.debug("Некорректный запрос: body='{}'.", response.getBody());
    }

    @AfterReturning(
            pointcut = "handleRuntimePointcutMethod()",
            returning = "response")
    public void logHandleRuntimeAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.error("Runtime ошибка: body='{}'.", response.getBody());
    }

    @AfterReturning(
            pointcut = "handleOtherPointcutMethod()",
            returning = "response")
    public void logHandleOtherAfterReturning(ResponseEntity<AuthErrorResponse> response) {
        log.error("Необработанная ошибка: body='{}'.", response.getBody());
    }
}
