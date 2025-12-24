package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.domain.redis.OneTimePassword;
import com.vdska.pointsapi2.dto.token.VerifyResponse;
import com.vdska.pointsapi2.repository.IOneTimePasswordRepository;
import com.vdska.pointsapi2.service.components.OTPGenerator;
import com.vdska.pointsapi2.service.spec.IOneTimePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OneTimePasswordService implements IOneTimePasswordService {
    @Value("${otp.ttl}")
    private Long ttl;

    private final IOneTimePasswordRepository oneTimePasswordRepository;
    private final OTPGenerator otpGenerator;
    private static final VerifyResponse INVALID_TOKEN = new VerifyResponse(false, null);

    /**
     * Метод для подтверждения корректности OTP.
     *
     * @param code код.
     */
    @Override
    public VerifyResponse verifyOTP(String code) {
        Optional<OneTimePassword> confirmationLinkOptional = oneTimePasswordRepository.findById(code);

        if (confirmationLinkOptional.isPresent()) {
            String username = confirmationLinkOptional.get().getUsername();
            oneTimePasswordRepository.deleteById(code);

            return new VerifyResponse(true, username);
        }

        return INVALID_TOKEN;
    }

    /**
     * Метод для генерации одноразового пароля.
     *
     * @param username имя пользователя, с аккаунтом которого будет связан одноразовый пароль.
     * @return сгенерированный OTP.
     */
    @Override
    public OneTimePassword generateOTP(String username) {
        String code = otpGenerator.generateCode();

        return new OneTimePassword(code, username, ttl);
    }

    /**
     * Метод для сохранения OTP в Redis.
     *
     * @param oneTimePassword OTP.
     */
    @Override
    public void saveOTP(OneTimePassword oneTimePassword) {
        oneTimePasswordRepository.save(oneTimePassword);
    }
}
