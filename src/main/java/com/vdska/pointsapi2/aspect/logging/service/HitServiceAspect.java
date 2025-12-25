package com.vdska.pointsapi2.aspect.logging.service;

import com.vdska.pointsapi2.dto.hit.AddHitRequest;
import com.vdska.pointsapi2.exception.CreditsException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class HitServiceAspect {

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IHitService.addPoint(..))")
    public void addPointPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IHitService.getPageOfHits(..))")
    public void getPageOfHitsPointcutMethod(){}

    @Pointcut("execution(public * com.vdska.pointsapi2.service.spec.IHitService.getHitsAmount(..))")
    public void getHitsAmountPointcutMethod(){}

    @Around("addPointPointcutMethod()")
    public Object logAddPoint(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String username = (String) pjp.getArgs()[0];
        AddHitRequest addHitRequest = (AddHitRequest) pjp.getArgs()[1];

        String x = addHitRequest.getX();
        String y = addHitRequest.getY();
        String r = addHitRequest.getR();

        log.debug("Началось добавление попадания: username='{}' x='{}' y='{}' r='{}'.", username, x, y, r);

        try {
            result = pjp.proceed();
            log.debug("Добавление попадания прошло успешно: username='{}' x='{}' y='{}' r='{}'.", username, x, y, r);
            return result;
        } catch (CreditsException creditsException) {
            log.debug("Добавление попадания завершилось неудачно: username='{}'. Причина: {}", username, creditsException.getMessage());
            throw creditsException;
        } finally {
            log.debug("Метод добавления попадания завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("getPageOfHitsPointcutMethod()")
    public Object logGetPageOfHits(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String username = (String) pjp.getArgs()[0];
        int limit = (int) pjp.getArgs()[1];
        int offset = (int) pjp.getArgs()[2];

        log.debug("Началось получение страницы попаданий: username='{}' limit='{}' offset='{}'.", username, limit, offset);

        try {
            result = pjp.proceed();
            log.debug("Страница попаданий была успешно получена: username='{}' limit='{}' offset='{}'.", username, limit, offset);
            return result;
        } catch (CreditsException creditsException) {
            log.debug("Получение страницы попаданий завершилось неудачно: username='{}'. Причина: {}", username, creditsException.getMessage());
            throw creditsException;
        } finally {
            log.debug("Метод получения страницы попаданий завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("getHitsAmountPointcutMethod()")
    public Object logGetHitsAmount(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String username = (String) pjp.getArgs()[0];

        log.debug("Начался подсчёт количества попаданий: username='{}'.", username);

        try {
            result = pjp.proceed();
            log.debug("Количество попаданий было успешно подсчитано: username='{}' amount='{}'.", username, result);
            return result;
        } finally {
            log.debug("Метод подсчёта количества попаданий завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }
}
