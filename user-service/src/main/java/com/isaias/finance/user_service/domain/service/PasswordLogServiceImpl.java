package com.isaias.finance.user_service.domain.service;

import com.isaias.finance.user_service.data.dto.PasswordUpdateDTO;
import com.isaias.finance.user_service.data.entity.PasswordLog;
import com.isaias.finance.user_service.data.entity.User;
import com.isaias.finance.user_service.data.repository.PasswordLogRepository;
import com.isaias.finance.user_service.domain.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordLogServiceImpl implements PasswordLogService {
    private final PasswordLogRepository repository;
    private final PasswordEncoder encoder;
    private final PasswordLogRepository passwordLogRepository;

    @Override
    public void logNewUserPassword(User user, String rawPassword, LocalDateTime timestamp) {
        PasswordLog log = new PasswordLog();
        log.setUser(user);
        log.setHashedPassword(encoder.encode(rawPassword));
        log.setAttemptTimestamp(timestamp);
        repository.save(log);
    }

    @Override
    public void logLoginError(User user, String errorDescription, LocalDateTime timestamp) {
        PasswordLog log = new PasswordLog();
        log.setUser(user);
        log.setHashedPassword("");
        log.setAttemptTimestamp(timestamp);
        log.setLoginError(errorDescription);
        repository.save(log);
    }

    @Override
    public void updatePassword(PasswordUpdateDTO dto, User user) {
        PasswordLog password = passwordLogRepository.findPasswordLogByUserOrderByAttemptTimestampDesc(user)
                .stream()
                .filter(log -> log.getLoginError() == null && log.getHashedPassword() != null)
                .findFirst()
                .orElseThrow(() -> new InvalidPasswordException("No valid password record found."));

        if (!encoder.matches(dto.getCurrentPassword(), password.getHashedPassword())) throw new BadCredentialsException("Incorrect current password.");

        if (encoder.matches(dto.getNewPassword(), password.getHashedPassword())) throw new InvalidPasswordException("The new password cannot be the same as the current password.");

        logNewUserPassword(user, dto.getNewPassword(), LocalDateTime.now());
    }
}