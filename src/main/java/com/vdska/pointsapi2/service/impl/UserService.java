package com.vdska.pointsapi2.service.impl;

import com.vdska.pointsapi2.domain.jpa.User;
import com.vdska.pointsapi2.dto.user.LoginResponse;
import com.vdska.pointsapi2.dto.user.RegisterRequest;
import com.vdska.pointsapi2.exception.CreditsException;
import com.vdska.pointsapi2.exception.InvalidCreditsOfConfirmationUserException;
import com.vdska.pointsapi2.exception.UserAlreadyExistsException;
import com.vdska.pointsapi2.mapper.IUserMapper;
import com.vdska.pointsapi2.repository.IUserRepository;
import com.vdska.pointsapi2.service.spec.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для работы с логикой процесса входа в приложение
 */
@Service
@AllArgsConstructor
public class UserService implements IUserService {
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

    /**
     * Метод для верификации пользователя в системе.
     *
     * @param username имя пользователя для верификации.
     */
    @Override
    public void verify(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setVerified(true);
            userRepository.save(user);
        } else {
            throw new InvalidCreditsOfConfirmationUserException("USER_NOT_FOUND");
        }
    }

    /**
     * Метод для получения пользователь с email.
     *
     * @param username имя пользователя, почту которого нужно получить.
     * @return почта пользователя.
     */
    @Override
    public String getEmail(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getEmail();
        } else {
            throw new CreditsException("USER_NOT_FOUND");
        }
    }

    /**
     * Метод для входа пользователем в аккаунт.
     *
     * @param username имя пользователя.
     * @param password пароль пользователя.
     */
    @Override
    public LoginResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new CreditsException("USER_NOT_FOUND");
        }

        String dbPassword = userOptional.get().getPassword();

        return new LoginResponse(passwordEncoder.matches(password, dbPassword), true);
    }
}
