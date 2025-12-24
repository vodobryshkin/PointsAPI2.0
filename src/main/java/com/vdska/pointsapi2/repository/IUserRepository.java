package com.vdska.pointsapi2.repository;

import com.vdska.pointsapi2.domain.jpa.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с пользователями
 */
@Repository
public interface IUserRepository extends CrudRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
}
