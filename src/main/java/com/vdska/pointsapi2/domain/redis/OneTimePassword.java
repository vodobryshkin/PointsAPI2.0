package com.vdska.pointsapi2.domain.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

/**
 * Сущность "Одноразовый пароль"
 */
@Getter
@Setter
@RedisHash("OneTimePassword")
@ToString
public class OneTimePassword implements Serializable {
    @Id
    private String code;

    private String username;

    private String challengeId;

    @TimeToLive
    private Long ttlSeconds;

    public OneTimePassword(String code, String username, String challengeId, Long ttlSeconds) {
        this.code = code;
        this.username = username;
        this.challengeId = challengeId;
        this.ttlSeconds = ttlSeconds;
    }
}
