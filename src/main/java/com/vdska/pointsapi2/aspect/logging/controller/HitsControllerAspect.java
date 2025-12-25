package com.vdska.pointsapi2.aspect.logging.controller;

import com.vdska.pointsapi2.dto.hit.AddHitRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class HitsControllerAspect {

    @Pointcut("execution(public * com.vdska.pointsapi2.controller.HitsController.addHit(..))")
    public void addHitPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.controller.HitsController.readPageOfHits(..))")
    public void readPageOfHitsPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.controller.HitsController.readAmountOfHits(..))")
    public void readAmountOfHitsPointcutMethod(){}

    @Around("addHitPointcutMethod()")
    public Object logAroundAddHit(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        AddHitRequest addHitRequest = (AddHitRequest) pjp.getArgs()[0];
        Authentication authentication = (Authentication) pjp.getArgs()[1];
        String username = authentication.getName();

        log.info("POST-запрос на эндпойнт /hits: username='{}' body='{}'.", username, addHitRequest);

        result = pjp.proceed();

        log.info("Эндпойнт /hits успешно вернул ответ для username='{}'.", username);
        return result;
    }

    @Around("readPageOfHitsPointcutMethod()")
    public Object logAroundReadPageOfHits(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        int limit = (int) pjp.getArgs()[0];
        int offset = (int) pjp.getArgs()[1];
        Authentication authentication = (Authentication) pjp.getArgs()[2];
        String username = authentication.getName();

        log.info("GET-запрос на эндпойнт /hits: username='{}' limit='{}' offset='{}'.", username, limit, offset);

        result = pjp.proceed();

        log.info("Эндпойнт /hits успешно вернул страницу hits для username='{}' limit='{}' offset='{}'.", username, limit, offset);
        return result;
    }

    @Around("readAmountOfHitsPointcutMethod()")
    public Object logAroundReadAmountOfHits(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        Authentication authentication = (Authentication) pjp.getArgs()[0];
        String username = authentication.getName();

        log.info("GET-запрос на эндпойнт /hits/amount: username='{}'.", username);

        result = pjp.proceed();

        log.info("Эндпойнт /hits/amount успешно вернул ответ для username='{}'.", username);
        return result;
    }
}
