package com.vdska.pointsapi2.controller;

import com.vdska.pointsapi2.dto.hit.AddHitRequest;
import com.vdska.pointsapi2.dto.hit.AddHitResponse;
import com.vdska.pointsapi2.dto.hit.GetHitsPageResponse;
import com.vdska.pointsapi2.service.spec.IHitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер, который обрабатывает запросы, приходящие на эндпойнт /hits/
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/hits")
public class HitsController {
    private final IHitService hitService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public AddHitResponse addHit(@RequestBody @Valid AddHitRequest addPointRequest,
                                                 Authentication authentication) {
        String username = authentication.getName();

        return hitService.addPoint(username, addPointRequest);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public GetHitsPageResponse readPageOfHits(@RequestParam(name = "limit",  defaultValue = "20") int limit,
                                              @RequestParam(name = "offset", defaultValue = "0") int offset,
                                              Authentication authentication) {
        String username = authentication.getName();

        return hitService.getPageOfHits(username, limit, offset);
    }

    @GetMapping("/amount")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> readAmountOfHits(Authentication authentication) {
        String username = authentication.getName();

        return ResponseEntity.ok().body(Map.of("amount", hitService.getHitsAmount(username)));
    }
}
