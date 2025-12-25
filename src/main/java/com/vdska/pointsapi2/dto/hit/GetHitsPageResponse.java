package com.vdska.pointsapi2.dto.hit;
import lombok.Value;

import java.util.List;

/**
 * DTO для описания полученных данных при запросе точек.
 */
@Value
public class GetHitsPageResponse {
    boolean status;

    List<HitDTO> hits;
}

