package com.vdska.pointsapi2.logger;

import com.vdska.pointsapi2.dto.auth.AuthErrorResponse;
import com.vdska.pointsapi2.dto.auth.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class AuthLoggingAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.controller.AuthController.register(..))")
    public void registerControllerPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.exception.AppExceptionHandler.handleRegisterException(..))")
    public void registerControllerAdvicePointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.impl.AuthService.register(..))")
    public void registerServicePointcutMethod(){}

    /**
     * Логгирование при запросе на эндпойнт /auth/register
     */
    @Before("registerControllerPointcutMethod()")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        RegisterRequest registerRequest = (RegisterRequest) joinPoint.getArgs()[0];
        log.info("POST-запрос на эндпойнт /auth/register: username='{}' email='{}'.",
                registerRequest.getUsername(), registerRequest.getEmail());
    }

    /**
     * Логгирование после успешного выполнения запроса
     */
    @AfterReturning(pointcut = "registerControllerPointcutMethod()")
    public void logRegisterAfterReturningControllerMethod() {
        log.info("Эндпойнт /auth/register успешно вернул ответ");
    }

    /**
     * Логгирование после исключения во время выполнения запроса
     */
    @AfterReturning(
            pointcut = "registerControllerAdvicePointcutMethod()",
            returning = "response")
    public void logRegisterAfterReturningControllerAdviceMethod(ResponseEntity<AuthErrorResponse> response) {
        AuthErrorResponse authResponse = response.getBody();
        log.info("Эндпойнт /auth/register вернул ответ c ошибкой: status='{}', body='{}'.",
                Objects.requireNonNull(authResponse).isStatus(), authResponse);
    }

    @Around("registerServicePointcutMethod()")
    public Object logRegisterServiceMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        RegisterRequest authRequest = (RegisterRequest) pjp.getArgs()[0];
        String username = authRequest.getUsername();
        String email = authRequest.getEmail();

        log.info("Началась обработка попытки регистрации пользователя с username='{}' и email='{}'.", username, email);

        try {
            result = pjp.proceed();
            log.info("Регистрация пользователя с username='{}' и email='{}' прошла успешно.", username, email);
            return result;
        } catch (UsernameNotFoundException usernameNotFoundException) {
            String reason = usernameNotFoundException.getMessage().equals("USERNAME_ALREADY_TAKEN") ? "username" : "email";
            log.info("Регистрация пользователя с username='{}' и email='{}' завершилась неудачно. Пользователь с таким {} уже существует.", username, email, reason);
            throw usernameNotFoundException;
        } finally {
            log.info("Метод регистрации завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }
}
