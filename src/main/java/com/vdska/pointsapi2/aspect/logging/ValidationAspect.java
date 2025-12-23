package com.vdska.pointsapi2.aspect.logging;

import com.vdska.pointsapi2.dto.auth.AuthErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ValidationAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleValidationException(..))")
    public void validationControllerAdvicePointcutMethod(){}

    @AfterReturning(
            pointcut = "validationControllerAdvicePointcutMethod()",
            returning = "response")
    public void logRegisterAfterReturningControllerAdviceMethod(ResponseEntity<AuthErrorResponse> response) {
        log.info("Запрос не прошёл валидацию: body='{}'.", response.getBody());
    }
}
