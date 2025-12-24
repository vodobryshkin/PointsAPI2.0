package com.vdska.pointsapi2.aspect.logging.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class JWTServiceAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IJWTService.generateAccessToken(..))")
    public void generateAccessTokenPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IJWTService.generateRefreshToken(..))")
    public void generateRefreshTokenPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IJWTService.isTokenValid(..))")
    public void isTokenValidPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IJWTService.extractUsername(..))")
    public void extractUsernamePointcutMethod(){}

    @Around("generateAccessTokenPointcutMethod()")
    public Object logGenerateAccessTokenPointcutMethod(ProceedingJoinPoint pjp) throws Throwable{
        Object result;

        String username = (String) pjp.getArgs()[0];
        Object roles = pjp.getArgs()[1].toString();

        log.debug("Начал генерацию access-токена для пользователя {} с ролями: {}.", username, roles);

        result = pjp.proceed();

        log.debug("Access-токен {} был успешно сгенерирован для пользователя {} с ролями: {}.", result, username, roles);
        return result;
    }

    @Around("generateRefreshTokenPointcutMethod()")
    public Object logGenerateRefreshTokenPointcutMethod(ProceedingJoinPoint pjp) throws Throwable{
        Object result;

        String username = (String) pjp.getArgs()[0];
        Object roles = pjp.getArgs()[1].toString();

        log.debug("Начал генерацию refresh-токена для пользователя {} с ролями: {}.", username, roles);

        result = pjp.proceed();

        log.debug("Refresh-токен {} был успешно сгенерирован для пользователя {} с ролями: {}.", result, username, roles);
        return result;
    }

    @Around("isTokenValidPointcutMethod()")
    public Object logIsTokenValidPointcutMethod(ProceedingJoinPoint pjp) throws Throwable{
        Object result;

        String token = (String) pjp.getArgs()[0];

        log.debug("Начал проверку токена: {}.", token);

        result = pjp.proceed();

        log.debug("Токен {} был успешно проверен. Статус: {}", token, result);
        return result;
    }

    @Around("extractUsernamePointcutMethod()")
    public Object logExtractUsernamePointcutMethod(ProceedingJoinPoint pjp) throws Throwable{
        Object result;

        String token = (String) pjp.getArgs()[0];

        log.debug("Начал вычленение имени пользователя из токена: {}.", token);

        result = pjp.proceed();

        log.debug("Имя {} было успешно получено из токена {}.", result, token);
        return result;
    }
}
