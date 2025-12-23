package com.vdska.pointsapi2.exception;

import com.vdska.pointsapi2.dto.auth.AuthErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обработчик ошибок, возникших за время работы программы
 */
@RestControllerAdvice
public class AppExceptionHandler {
    private static final AuthErrorResponse USERNAME_TAKEN =
            new AuthErrorResponse(false, "USERNAME_ALREADY_TAKEN", null);
    private static final AuthErrorResponse EMAIL_TAKEN =
            new AuthErrorResponse(false, "EMAIL_ALREADY_TAKEN", null);
    private static final AuthErrorResponse BAD_REQUEST =
            new AuthErrorResponse(false, "BAD_REQUEST", null);
    
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
}
