package com.vdska.pointsapi2.exception;

public class ConfirmationLinkNotValidException extends RuntimeException {
    public ConfirmationLinkNotValidException(String message) {
        super(message);
    }
}
