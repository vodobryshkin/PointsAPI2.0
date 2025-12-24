package com.vdska.pointsapi2.controller;

import com.vdska.pointsapi2.domain.redis.ConfirmationLink;
import com.vdska.pointsapi2.dto.confirm.VerifyResponse;
import com.vdska.pointsapi2.dto.user.RegisterRequest;
import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import com.vdska.pointsapi2.exception.ConfirmationLinkNotValidException;
import com.vdska.pointsapi2.service.spec.IJWTService;
import com.vdska.pointsapi2.service.spec.IUserService;
import com.vdska.pointsapi2.service.spec.IConfirmationLinkService;
import com.vdska.pointsapi2.service.spec.IMessageService;
import com.vdska.pointsapi2.validaton.formats.uuid.UUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, который обрабатывает запросы, приходящие на эндпойнт /auth/register
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final IConfirmationLinkService confirmationLinkService;
    private final IMessageService messageService;
    private final IJWTService jwtService;

    @Value("${auth.registration.role}")
    private String registrationRole;

    @Value("${auth.verification.role}")
    private String verificationRole;

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

        return verifyResponseWithTokens(registerRequest.getUsername(), registrationRole);
    }

    /**
     * Метод для обработки GET-запросов на эндпойнт /auth/confirm
     *
     * @return пустой ResponseEntity, так как возникшую ошибку отловит controller advice.
     */
    @Validated
    @GetMapping("/auth/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> confirm(@RequestParam(name = "id") @UUID String id) {
        VerifyResponse verifyResponse = confirmationLinkService.verifyConfirmationLink(id);

        if (!verifyResponse.isStatus()) {
            throw new ConfirmationLinkNotValidException("CONFIRMATION_LINK_NOT_VALID");
        }

        userService.verify(verifyResponse.getUsername());

        return verifyResponseWithTokens(verifyResponse.getUsername(), verificationRole);
    }

    private ResponseEntity<Void> verifyResponseWithTokens(String username, String role) {
        String accessToken = jwtService.generateAccessToken(username, role);
        String refreshToken = jwtService.generateRefreshToken(username, role);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }
}
