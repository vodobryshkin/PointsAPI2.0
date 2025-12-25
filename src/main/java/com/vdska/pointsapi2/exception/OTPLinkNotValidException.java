package com.vdska.pointsapi2.exception;

public class OTPLinkNotValidException extends RuntimeException {
    public OTPLinkNotValidException(String message) {
        super(message);
    }
}
