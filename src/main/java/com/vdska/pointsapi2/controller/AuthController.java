package com.vdska.pointsapi2.controller;

import com.vdska.pointsapi2.domain.redis.ConfirmationLink;
import com.vdska.pointsapi2.domain.redis.OneTimePassword;
import com.vdska.pointsapi2.dto.mail.OTPMailRequest;
import com.vdska.pointsapi2.dto.token.OTPRequest;
import com.vdska.pointsapi2.dto.token.VerifyResponse;
import com.vdska.pointsapi2.dto.user.LoginRequest;
import com.vdska.pointsapi2.dto.user.LoginResponse;
import com.vdska.pointsapi2.dto.user.RegisterRequest;
import com.vdska.pointsapi2.dto.mail.ConfirmAccountMailRequest;
import com.vdska.pointsapi2.exception.VerifyException;
import com.vdska.pointsapi2.exception.CreditsException;
import com.vdska.pointsapi2.service.spec.*;
import com.vdska.pointsapi2.validaton.formats.uuid.UUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер, который обрабатывает запросы, приходящие на эндпойнт /auth/register
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final IUserService userService;
    private final IConfirmationLinkService confirmationLinkService;
    private final IMessageService messageService;
    private final IJWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final IOneTimePasswordService oneTimePasswordService;

    /**
     * Метод для обработки запросов на эндпойнт /auth/register
     *
     * @param registerRequest данные на регистрацию аккаунта.
     * @return пустой ResponseEntity, так как возникшую ошибку отловит controller advice.
     */
    @PostMapping("/register")
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

        return verifyResponseWithTokens(registerRequest.getUsername());
    }

    /**
     * Метод для обработки запросов на эндпойнт /auth/login
     *
     * @param loginRequest данные на вход в аккаунт.
     * @return пустой ResponseEntity, так как возникшую ошибку отловит controller advice.
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                               @RequestHeader(value = "User-Agent", required = false) String userAgent,
                                               @RequestHeader(value = "Date", required = false) String dateHeader) {
        LoginResponse loginResponse = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (!loginResponse.isStatus()) {
            throw new CreditsException("PASSWORD_NOT_MATCHES");
        }

        OneTimePassword oneTimePassword = oneTimePasswordService.generateOTP(loginRequest.getUsername(), loginResponse.getChallengeId());
        oneTimePasswordService.saveOTP(oneTimePassword);

        String email = userService.getEmail(loginRequest.getUsername());

        messageService.sendOTPMessageToQueue(new OTPMailRequest(
                loginRequest.getUsername(),
                email,
                "Вход в аккаунт",
                oneTimePassword.getCode(),
                dateHeader,
                userAgent));

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    /**
     * Метод для обработки POST-запроса на эндпойнт /auth/otp
     *
     * @param otpRequest запрос с отправленным otp-кодом
     */
    @PostMapping("/otp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> otp(@RequestBody @Valid OTPRequest otpRequest) {
        VerifyResponse verifyResponse = oneTimePasswordService.verifyOTP(otpRequest);

        if (!verifyResponse.isStatus()) {
            throw new VerifyException(verifyResponse.getMessage());
        }

        return verifyResponseWithTokens(verifyResponse.getMessage());
    }

    /**
     * Метод для обработки GET-запросов на эндпойнт /auth/confirm
     *
     * @return пустой ResponseEntity, так как возникшую ошибку отловит controller advice.
     */
    @Validated
    @GetMapping("/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> confirm(@RequestParam(name = "id") @UUID String id) {
        VerifyResponse verifyResponse = confirmationLinkService.verifyConfirmationLink(id);

        if (!verifyResponse.isStatus()) {
            throw new VerifyException("LINK_NOT_VALID");
        }

        userService.verify(verifyResponse.getMessage());

        return verifyResponseWithTokens(verifyResponse.getMessage());
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('UNVERIFIED_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> resendConfirmLetter(Authentication authentication) {
        String email = userService.getEmail(authentication.getName());

        ConfirmationLink confirmationLink = confirmationLinkService.generateConfirmationLink(authentication.getName());
        confirmationLinkService.saveConfirmationLink(confirmationLink);

        messageService.sendConfirmAccountMessageToQueue(new ConfirmAccountMailRequest(
                authentication.getName(),
                email,
                "Подтвердите ваш аккаунт",
                confirmationLink.getId().toString()));

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(name = "refresh_token") String refreshToken) {
        if (jwtService.isTokenValid(refreshToken)) {
            String username = jwtService.extractUsername(refreshToken);
            List<String> roles = getUserRoles(username);

            String accessToken = jwtService.generateAccessToken(username, roles);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .header(HttpHeaders.SET_COOKIE, refreshToken)
                    .build();
        }

        return ResponseEntity
                .status(401)
                .build();
    }

    private List<String> getUserRoles(String username) {
        UserDetails ud = userDetailsService.loadUserByUsername(username);
        return  ud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private ResponseEntity<Void> verifyResponseWithTokens(String username) {
        List<String> roles = getUserRoles(username);

        String accessToken = jwtService.generateAccessToken(username, roles);
        String refreshToken = jwtService.generateRefreshToken(username, roles);

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
