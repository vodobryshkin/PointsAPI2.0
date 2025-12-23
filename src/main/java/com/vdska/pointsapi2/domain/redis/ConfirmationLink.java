package com.vdska.pointsapi2.domain.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.UUID;

/**
 * Сущность "Ссылка для подтверждения аккаунта"
 */
@Getter
@Setter
@RedisHash("ConfirmationLink")
public class ConfirmationLink implements Serializable {
    @Id
    private UUID id;

    private String username;

    private boolean isActivated;

    @TimeToLive
    private Long ttlSeconds;

    public ConfirmationLink(UUID id, String username, Long ttlSeconds) {
        this.id = id;
        isActivated = false;
        this.username = username;
        this.ttlSeconds = ttlSeconds;
    }
}
