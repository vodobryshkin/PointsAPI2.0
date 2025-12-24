package com.vdska.pointsapi2.aspect.logging.controller;

import com.vdska.pointsapi2.dto.user.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthControllerAspect {
    @Pointcut("execution(public * com.vdska.pointsapi2.controller.AuthController.register(..))")
    public void registerPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.controller.AuthController.confirm(..))")
    public void confirmPointcutMethod(){}

    @Before("registerPointcutMethod()")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        RegisterRequest registerRequest = (RegisterRequest) joinPoint.getArgs()[0];
        log.info("POST-запрос на эндпойнт /auth/register: username='{}' email='{}'.",
                registerRequest.getUsername(), registerRequest.getEmail());
    }

    @AfterReturning(pointcut = "registerPointcutMethod()")
    public void logRegisterAfterReturningControllerMethod() {
        log.info("Эндпойнт /auth/register успешно вернул ответ.");
    }

    @Before("confirmPointcutMethod()")
    public void logConfirmBefore(JoinPoint joinPoint) {
        String id = (String) joinPoint.getArgs()[0];
        log.info("GET-запрос на эндпойнт /auth/confirm: id='{}'.", id);
    }

    @AfterReturning(pointcut = "confirmPointcutMethod()")
    public void logConfirmAfterReturning() {
        log.info("Эндпойнт /auth/confirm успешно вернул ответ.");
    }
}
