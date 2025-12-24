package com.vdska.pointsapi2.repository;

import com.vdska.pointsapi2.domain.redis.OneTimePassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с OTP
 */
@Repository
public interface IOneTimePasswordRepository extends CrudRepository<OneTimePassword, String> {
}