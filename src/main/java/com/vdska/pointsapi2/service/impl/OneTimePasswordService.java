package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.domain.redis.ChallengeId;
import com.vdska.pointsapi2.domain.redis.OneTimePassword;
import com.vdska.pointsapi2.dto.token.OTPRequest;
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
    private final ChallengeIdService challengeIdService;
    private static final VerifyResponse INVALID_TOKEN = new VerifyResponse(false, "OTP_NOT_VALID");

    /**
     * Метод для подтверждения корректности OTP.
     *
     * @param otpRequest запрос на подтверждение.
     */
    @Override
    public VerifyResponse verifyOTP(OTPRequest otpRequest)
    {
        Optional<OneTimePassword> oneTimePassword = oneTimePasswordRepository.findById(otpRequest.getCode());

        if (oneTimePassword.isEmpty()) {
            return INVALID_TOKEN;
        }

        String username = oneTimePassword.get().getUsername();
        VerifyResponse verifyResponse = challengeIdService.verifyChallengeId(username, otpRequest.getChallengeId());

        if (verifyResponse.isStatus()) {
            oneTimePasswordRepository.deleteById(otpRequest.getCode());

            return new VerifyResponse(true, username);
        }

        return verifyResponse;
    }

    /**
     * Метод для генерации одноразового пароля.
     *
     * @param username имя пользователя, с аккаунтом которого будет связан одноразовый пароль.
     * @return сгенерированный OTP.
     */
    @Override
    public OneTimePassword generateOTP(String username, String challengeID) {
        String code = otpGenerator.generateCode();

        ChallengeId challengeId = challengeIdService.generateChallengeId(username, challengeID);
        challengeIdService.saveChallengeId(challengeId);

        return new OneTimePassword(code, username, challengeId.getId(), ttl);
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
