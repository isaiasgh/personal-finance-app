package com.isaias.finance.user_service.domain.service;

import com.isaias.finance.user_service.data.entity.PasswordLog;
import com.isaias.finance.user_service.data.entity.User;
import com.isaias.finance.user_service.data.repository.PasswordLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordLogServiceImpl implements PasswordLogService {
    private final PasswordLogRepository repository;
    private final PasswordEncoder encoder;

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
}