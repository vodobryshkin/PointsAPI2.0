package com.vdska.pointsapi2.aspect.logging.service;

import checkhit.dto.AreasFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CheckoutHitServiceAspect {

    @Pointcut("execution(public * checkhit.service.ACheckoutHitService.checkoutHit(..))")
    public void checkoutHitPointcutMethod(){}

    @Pointcut("execution(public * checkhit.service.ACheckoutHitService.updateResource(..))")
    public void updateResourcePointcutMethod(){}

    @Pointcut("execution(public * checkhit.service.ACheckoutHitService.getAreasData(..))")
    public void getAreasDataPointcutMethod(){}

    @Around("checkoutHitPointcutMethod()")
    public Object logCheckoutHit(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        String x = (String) pjp.getArgs()[0];
        String y = (String) pjp.getArgs()[1];
        String r = (String) pjp.getArgs()[2];

        log.debug("Началась проверка попадания: x='{}' y='{}' r='{}'.", x, y, r);

        try {
            result = pjp.proceed();
            log.debug("Проверка попадания завершилась успешно: x='{}' y='{}' r='{}' result='{}'.", x, y, r, result);
            return result;
        } catch (RuntimeException ex) {
            log.debug("Проверка попадания завершилась неудачно: x='{}' y='{}' r='{}'. Причина: {}", x, y, r, ex.getMessage());
            throw ex;
        } finally {
            log.debug("Метод проверки попадания завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("updateResourcePointcutMethod()")
    public Object logUpdateResource(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        AreasFileDTO areasFileDTO = (AreasFileDTO) pjp.getArgs()[0];

        log.debug("Началось обновление ресурса областей: areas_file='{}'.", areasFileDTO);

        try {
            result = pjp.proceed();
            log.debug("Обновление ресурса областей прошло успешно.");
            return result;
        } catch (RuntimeException ex) {
            log.debug("Обновление ресурса областей завершилось неудачно. Причина: {}", ex.getMessage());
            throw ex;
        } finally {
            log.debug("Метод обновления ресурса областей завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }

    @Around("getAreasDataPointcutMethod()")
    public Object logGetAreasData(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        long startTime = System.currentTimeMillis();

        log.debug("Началось получение данных областей.");

        try {
            result = pjp.proceed();
            log.debug("Данные областей были успешно получены: {}.", result);
            return result;
        } catch (RuntimeException ex) {
            log.debug("Получение данных областей завершилось неудачно. Причина: {}", ex.getMessage());
            throw ex;
        } finally {
            log.debug("Метод получения данных областей завершился. Время выполнения {} ms.", System.currentTimeMillis() - startTime);
        }
    }
}
