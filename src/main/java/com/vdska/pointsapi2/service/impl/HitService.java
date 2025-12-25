package com.vdska.pointsapi2.service.impl;

import checkhit.service.ICheckoutHitService;
import com.vdska.pointsapi2.domain.jpa.Hit;
import com.vdska.pointsapi2.domain.jpa.User;
import com.vdska.pointsapi2.dto.hit.AddHitRequest;
import com.vdska.pointsapi2.dto.hit.AddHitResponse;
import com.vdska.pointsapi2.dto.hit.GetHitsPageResponse;
import com.vdska.pointsapi2.exception.auth.CreditsException;
import com.vdska.pointsapi2.mapper.IHitMapper;
import com.vdska.pointsapi2.repository.IHitRepository;
import com.vdska.pointsapi2.repository.IUserRepository;
import com.vdska.pointsapi2.service.spec.IHitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с попаданиями.
 */
@Service
@RequiredArgsConstructor
public class HitService implements IHitService {
    private final IUserRepository userRepository;
    private final IHitRepository hitRepository;
    private final IHitMapper hitMapper;
    private final ICheckoutHitService checkoutHitService;

    /**
     * Метод сервиса для добавления точки.
     *
     * @param username      имя пользователя.
     * @param addHitRequest DTO с данными для добавления точки.
     * @return результат добавления.
     */
    @Override
    public AddHitResponse addPoint(String username, AddHitRequest addHitRequest) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new CreditsException("USER_NOT_FOUND");
        }

        String x = addHitRequest.getX(), y = addHitRequest.getY(), r = addHitRequest.getR();

        User user = userOptional.get();
        Hit hit = new Hit(x, y, r, checkoutHitService.checkoutHit(x, y, r), user);

        hitRepository.save(hit);

        return new AddHitResponse(true, "OK", hitMapper.toDTO(hit));
    }

    /**
     * Метод сервиса для добавления страницы попаданий из базы данных.
     *
     * @param username имя пользователя.
     * @param limit    limit.
     * @param offset   offset.
     * @return результат получения страницы точек.
     */
    @Override
    public GetHitsPageResponse getPageOfHits(String username, int limit, int offset) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new CreditsException("USER_NOT_FOUND");
        }

        UUID uuid = userOptional.get().getId();
        int page = offset / limit;

        Page<Hit> hits = hitRepository.findByUserIdOrderByDateTimeDesc(
                uuid,
                PageRequest.of(page, limit, Sort.by("dateTime").descending()));

        return new GetHitsPageResponse(true, hits.getContent().stream().map(hitMapper::toDTO).toList());
    }

    /**
     * Метод сервиса для подсчёта попаданий конкретного пользователя из базы данных.
     *
     * @param username имя пользователя.
     * @return количество попаданий.
     */
    @Override
    public long getHitsAmount(String username) {
        return hitRepository.countByUserUsername(username);
    }
}
