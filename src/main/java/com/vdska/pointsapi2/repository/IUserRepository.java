package com.vdska.pointsapi2.repository;

import com.vdska.pointsapi2.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с пользователями
 */
public interface IUserRepository extends CrudRepository<User, UUID> {
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
}
