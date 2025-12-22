package com.vdska.pointsapi2.mapper;

import com.vdska.pointsapi2.domain.User;
import com.vdska.pointsapi2.dto.auth.RegisterRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Маппер для перевода из DTO ответа на запрос в сущность "Пользователь"
 */
@Mapper(componentModel = "spring")
public interface IUserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerRequest.getPassword()))")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "verified", constant = "false")
    User toEntity(RegisterRequest registerRequest, @Context PasswordEncoder passwordEncoder);
}
