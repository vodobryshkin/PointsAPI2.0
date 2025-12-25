package com.vdska.pointsapi2.repository;

import com.vdska.pointsapi2.domain.redis.ChallengeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с токенами для подписи OTP
 */
@Repository
public interface IChallengeIdRepository extends CrudRepository<ChallengeId, String> {
}
