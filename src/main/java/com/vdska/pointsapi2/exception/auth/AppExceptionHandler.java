package com.vdska.pointsapi2.exception.auth;

import com.vdska.pointsapi2.dto.user.AuthErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработчик ошибок, возникших за время работы программы
 */
@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {
    private static final AuthErrorResponse USERNAME_TAKEN =
            new AuthErrorResponse(false, "USERNAME_ALREADY_TAKEN", null);
    private static final AuthErrorResponse EMAIL_TAKEN =
            new AuthErrorResponse(false, "EMAIL_ALREADY_TAKEN", null);
    private static final AuthErrorResponse BAD_REQUEST =
            new AuthErrorResponse(false, "BAD_REQUEST", null);
    private static final AuthErrorResponse CONFIRMATION_LINK_NOT_VALID =
            new AuthErrorResponse(false, "LINK_NOT_VALID", null);
    private static final AuthErrorResponse USER_NOT_FOUND =
            new AuthErrorResponse(false, "USER_NOT_FOUND", null);
    private static final AuthErrorResponse PASSWORD_NOT_MATCHES =
            new AuthErrorResponse(false, "PASSWORD_NOT_MATCHES", null);
    private static final AuthErrorResponse OTP_NOT_VALID =
            new AuthErrorResponse(false, "OTP_NOT_VALID", null);
    private static final AuthErrorResponse CHALLENGE_ID_NOT_VALID =
            new AuthErrorResponse(false, "CHALLENGE_ID_NOT_VALID", null);


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<AuthErrorResponse> handleRegisterException(UserAlreadyExistsException e) {
        String message = e.getMessage();

        return switch (message) {
            case "USERNAME_ALREADY_TAKEN" -> new ResponseEntity<>(USERNAME_TAKEN, HttpStatus.CONFLICT);
            case "EMAIL_ALREADY_TAKEN" -> new ResponseEntity<>(EMAIL_TAKEN, HttpStatus.CONFLICT);
            default -> new ResponseEntity<>(BAD_REQUEST, HttpStatus.BAD_REQUEST);
        };
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));


        return new ResponseEntity<>(new AuthErrorResponse(false, "VALIDATION_ERROR", errors),
                HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<AuthErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {

        Map<String, List<String>> errors = ex.getAllErrors().stream()
                .collect(Collectors.groupingBy(
                        err -> {
                            if (err instanceof FieldError fe) return fe.getField();
                            if (err instanceof ObjectError oe) return oe.getObjectName();
                            return "request";
                        },
                        LinkedHashMap::new,
                        Collectors.mapping(
                                MessageSourceResolvable::getDefaultMessage,
                                Collectors.toList()
                        )
                ));

        return new ResponseEntity<>(new AuthErrorResponse(false, "VALIDATION_ERROR", errors),
                HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(VerifyException.class)
    public ResponseEntity<AuthErrorResponse> handleVerifyException(VerifyException e) {
        log.info(e.getMessage());
        return switch (e.getMessage()) {
            case "OTP_NOT_VALID" -> new ResponseEntity<>(OTP_NOT_VALID, HttpStatus.GONE);
            case "CHALLENGE_ID_NOT_VALID" -> new ResponseEntity<>(CHALLENGE_ID_NOT_VALID, HttpStatus.GONE);
            case "LINK_NOT_VALID" -> new ResponseEntity<>(CONFIRMATION_LINK_NOT_VALID, HttpStatus.NOT_FOUND);
            default -> new ResponseEntity<>(BAD_REQUEST, HttpStatus.BAD_REQUEST);
        };
    }

    @ExceptionHandler(InvalidCreditsOfConfirmationUserException.class)
    public ResponseEntity<AuthErrorResponse> handleInvalidCreditsOfConfirmationUserException(InvalidCreditsOfConfirmationUserException e) {
        String message = e.getMessage();

        if (message.equals("USER_NOT_FOUND")) {
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(CreditsException.class)
    public ResponseEntity<AuthErrorResponse> handleCreditsException(CreditsException e) {
        String message = e.getMessage();

        return switch (message) {
            case "USER_NOT_FOUND" -> new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            case "PASSWORD_NOT_MATCHES" -> new ResponseEntity<>(PASSWORD_NOT_MATCHES, HttpStatus.UNAUTHORIZED);
            default -> new ResponseEntity<>(BAD_REQUEST, HttpStatus.BAD_REQUEST);
        };
    }
}
