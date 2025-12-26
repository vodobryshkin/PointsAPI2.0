package com.vdska.pointsapi2.controller;

import checkhit.dto.AreasFileDTO;
import checkhit.service.ACheckoutHitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, который обрабатывает запросы, приходящие на эндпойнт /areas
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/areas")
public class AdminController {
    private final ACheckoutHitService checkoutHitService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public AreasFileDTO readAreas() {
        return checkoutHitService.getAreasData();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> sendAreas(@RequestBody AreasFileDTO areasFileDTO) {
        checkoutHitService.updateResource(areasFileDTO);

        return ResponseEntity
                .ok()
                .build();
    }
}
