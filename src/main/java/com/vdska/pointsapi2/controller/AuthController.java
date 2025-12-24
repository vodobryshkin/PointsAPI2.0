package com.vdska.pointsapi2.controller;

import com.vdska.pointsapi2.domain.redis.ConfirmationLink;
import com.vdska.pointsapi2.dto.auth.RegisterRequest;
import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import com.vdska.pointsapi2.service.spec.IUserService;
import com.vdska.pointsapi2.service.spec.IConfirmationLinkService;
import com.vdska.pointsapi2.service.spec.IMessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, который обрабатывает запросы, приходящие на эндпойнт /auth/register
 */
@RestController
@AllArgsConstructor
public class AuthController {
    private IUserService userService;
    private IConfirmationLinkService confirmationLinkService;
    private IMessageService messageService;

    /**
     * Метод для обработки запросов на эндпойнт /auth/register
     *
     * @param registerRequest данные на регистрацию аккаунта.
     * @return пустой ResponseEntity, так как возникшую ошибку отловит controller advice.
     */
    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest registerRequest) {
        userService.register(registerRequest);

        ConfirmationLink confirmationLink = confirmationLinkService.generateConfirmationLink(registerRequest.getUsername());
        confirmationLinkService.saveConfirmationLink(confirmationLink);

        messageService.sendConfirmAccountMessageToQueue(new ConfirmAccountMailRequest(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                "Подтвердите ваш аккаунт",
                confirmationLink.getId().toString()));

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * Метод для обработки GET-запросов на эндпойнт /auth/confirm
     *
     * @return пустой ResponseEntity, так как возникшую ошибку отловит controller advice.
     */
    @GetMapping("/auth/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> confirm(@RequestParam(name = "id") String id) {

        return ResponseEntity
                .noContent()
                .build();
    }
}
