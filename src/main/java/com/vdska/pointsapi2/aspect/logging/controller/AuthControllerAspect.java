package com.vdska.pointsapi2.aspect.logging.controller;

import com.vdska.pointsapi2.dto.user.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthControllerAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.controller.AuthController.register(..))")
    public void registerPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.controller.AuthController.confirm(..))")
    public void confirmPointcutMethod(){}

    @Around("registerPointcutMethod()")
    public Object logAroundRegister(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        RegisterRequest registerRequest = (RegisterRequest) pjp.getArgs()[0];

        log.info("POST-запрос на эндпойнт /auth/register: username='{}' email='{}'.", registerRequest.getUsername(), registerRequest.getEmail());

        result = pjp.proceed();

        log.info("Эндпойнт /auth/register успешно вернул ответ. Пользователь {} был успешно зарегистрирован.", registerRequest.getUsername());

        return result;
    }

    @Around("confirmPointcutMethod()")
    public Object logAroundConfirm(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        String id = (String) pjp.getArgs()[0];

        log.info("GET-запрос на эндпойнт /auth/confirm: id='{}'.", id);

        result = pjp.proceed();

        log.info("Эндпойнт /auth/confirm успешно вернул ответ для id={}.", id);
        return result;
    }
}
