package com.vdska.pointsapi2.service.spec;

import com.vdska.pointsapi2.dto.hit.AddHitRequest;
import com.vdska.pointsapi2.dto.hit.AddHitResponse;
import com.vdska.pointsapi2.dto.hit.GetHitsPageResponse;

/**
 * Интерфейс для определения функциональности HitService
 */
public interface IHitService {
    /**
     * Метод сервиса для добавления точки.
     *
     * @param username имя пользователя.
     * @param addHitRequest DTO с данными для добавления точки.
     * @return результат добавления.
     */
    AddHitResponse addPoint(String username, AddHitRequest addHitRequest);

    /**
     * Метод сервиса для добавления страницы попаданий из базы данных.
     *
     * @param username имя пользователя.
     * @param limit limit.
     * @param offset offset.
     * @return результат получения страницы точек.
     */
    GetHitsPageResponse getPageOfHits(String username, int limit, int offset);

    /**
     * Метод сервиса для подсчёта попаданий конкретного пользователя из базы данных.
     *
     * @param username имя пользователя.
     * @return количество попаданий.
     */
    long getHitsAmount(String username);
}
