package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.dto.auth.RegisterRequest;
import com.vdska.pointsapi2.exception.UserAlreadyExistsException;
import com.vdska.pointsapi2.mapper.IUserMapper;
import com.vdska.pointsapi2.repository.IUserRepository;
import com.vdska.pointsapi2.service.spec.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с логикой процесса входа в приложение
 */
@Service
@AllArgsConstructor
public class AuthService implements IAuthService {
    private IUserRepository userRepository;
    private IUserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    /**
     * Метод для регистрации нового пользователя в системе.
     * @param registerRequest запрос на регистрацию пользователя.
     */
    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsUserByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("USERNAME_ALREADY_TAKEN");
        }
        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("EMAIL_ALREADY_TAKEN");
        }

        userRepository.save(userMapper.toEntity(registerRequest, passwordEncoder));
    }
}
