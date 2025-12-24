package com.vdska.pointsapi2.repository;

import com.vdska.pointsapi2.domain.redis.ConfirmationLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с токенами для подтверждения аккаунта
 */
@Repository
public interface IConfirmationLinkRepository extends CrudRepository<ConfirmationLink, UUID> {
}
