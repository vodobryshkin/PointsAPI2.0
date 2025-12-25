package com.vdska.pointsapi2.repository;

import com.vdska.pointsapi2.domain.jpa.Hit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с попаданиями.
 */
@Repository
public interface IHitRepository extends JpaRepository<Hit, UUID> {
    Page<Hit> findByUserIdOrderByDateTimeDesc(UUID uuid, Pageable pageable);
    long countByUserUsername(String username);
}
