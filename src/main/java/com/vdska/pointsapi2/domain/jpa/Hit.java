package com.vdska.pointsapi2.domain.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность "Попадание на координатную плоскость"
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hits")
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @JsonIgnore
    private UUID id;

    @Column(name = "x")
    private BigDecimal x;

    @Column(name = "y")
    private BigDecimal y;

    @Column(name = "r")
    private BigDecimal r;

    @Column(name = "status")
    private boolean status;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy.MM.dd'T'HH:mm:ss"
    )
    @JsonProperty("date_of_creation")
    @Column(name = "date_of_creation")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Hit(String x, String y, String r, boolean status, User user) {
        this.x = new BigDecimal(x);
        this.y = new BigDecimal(y);
        this.r = new BigDecimal(r);
        this.user = user;
        this.status = status;
        dateTime = LocalDateTime.now();
    }
}
