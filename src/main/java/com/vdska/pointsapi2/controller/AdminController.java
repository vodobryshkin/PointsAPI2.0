package com.vdska.pointsapi2.controller;

import checkhit.dto.AreasFileDTO;
import checkhit.service.ACheckoutHitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, который обрабатывает запросы, приходящие на эндпойнт /auth/admin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ACheckoutHitService checkoutHitService;

    @GetMapping("/areas")
    @PreAuthorize("hasRole('ADMIN')")
    public AreasFileDTO readAreas() {
        return checkoutHitService.getAreasData();
    }

    @PostMapping("/areas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> sendAreas(@RequestBody AreasFileDTO areasFileDTO) {
        checkoutHitService.updateResource(areasFileDTO);

        return ResponseEntity
                .ok()
                .build();
    }
}
