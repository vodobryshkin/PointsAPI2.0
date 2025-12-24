package com.vdska.pointsapi2.service.components;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OTPGenerator {
    private static final SecureRandom SR = new SecureRandom();

    public String generateCode() {
        int n = SR.nextInt(1_000_000);
        return String.format("%06d", n);
    }
}

