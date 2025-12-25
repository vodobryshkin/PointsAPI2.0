package com.vdska.pointsapi2.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * Сущность "Ссылка для подтверждения аккаунта"
 */
@Getter
@Setter
@RedisHash("ConfirmationLink")
@AllArgsConstructor
public class ChallengeId {
    @Id
    private String username;

    private String id;

    @TimeToLive
    private Long ttl;

}
