package com.vdska.pointsapi2.mapper;

import com.vdska.pointsapi2.domain.jpa.Hit;
import com.vdska.pointsapi2.dto.hit.HitDTO;
import org.mapstruct.Mapper;

/**
 * Маппер для перевода из DTO ответа на запрос в сущность "Попадание"
 */
@Mapper(componentModel = "spring")
public interface IHitMapper {
    HitDTO toDTO(Hit hit);
}
