package com.vdska.pointsapi2.aspect.logging.service;

import com.vdska.pointsapi2.dto.user.RegisterRequest;
import com.vdska.pointsapi2.exception.CreditsException;
import com.vdska.pointsapi2.exception.InvalidCreditsOfConfirmationUserException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserServiceAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IUserService.register(..))")
    public void registerPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IUserService.verify(..))")
    public void verifyPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IUserService.getEmail(..))")
    public void getEmailPointcutMethod(){}

    @Around("registerPointcutMethod()")
    public Object logRegister(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        RegisterRequest authRequest = (RegisterRequest) pjp.getArgs()[0];
        String username = authRequest.getUsername();
        String email = authRequest.getEmail();

        log.debug("Началась обработка попытки регистрации пользователя с username='{}' и email='{}'.", username, email);

        try {
            result = pjp.proceed();
            log.debug("Регистрация пользователя с username='{}' и email='{}' прошла успешно.", username, email);
            return result;
        } catch (UsernameNotFoundException usernameNotFoundException) {
            String reason = usernameNotFoundException.getMessage().equals("USERNAME_ALREADY_TAKEN") ? "username" : "email";
            log.debug("Регистрация пользователя с username='{}' и email='{}' завершилась неудачно. Пользователь с таким {} уже существует.", username, email, reason);
            throw usernameNotFoundException;
        } finally {
            log.debug("Метод регистрации завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("verifyPointcutMethod()")
    public Object logVerify(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String username = (String) pjp.getArgs()[0];

        log.debug("Началось подтверждение аккаунта пользователя с username='{}'.", username);

        try {
            result = pjp.proceed();
            log.debug("Аккаунт пользователя с username='{}' был успешно подтверждён.", username);
            return result;
        } catch (InvalidCreditsOfConfirmationUserException usernameNotFoundException) {
            log.debug("Подтверждение аккаунта пользователя с username='{}' завершилось неудачно. Пользователя с таким username не существует.", username);
            throw usernameNotFoundException;
        } finally {
            log.debug("Метод подтверждения аккаунта пользователя завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("getEmailPointcutMethod()")
    public Object logGetEmail(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String username = (String) pjp.getArgs()[0];

        log.debug("Началось получение почты пользователя с с username='{}'.", username);

        try {
            result = pjp.proceed();
            log.debug("Почта пользователя с username='{}' была успешна получена: {}.", username, result);
            return result;
        } catch (CreditsException usernameNotFoundException) {
            log.debug("Получение почты пользователя с username='{}' завершилось неудачно. Пользователя с таким username не существует.", username);
            throw usernameNotFoundException;
        } finally {
            log.debug("Метод получения почты пользователя завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }
}
