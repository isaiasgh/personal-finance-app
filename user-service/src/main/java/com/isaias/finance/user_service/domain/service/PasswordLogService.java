package com.isaias.finance.user_service.domain.service;

import com.isaias.finance.user_service.data.entity.User;

import java.time.LocalDateTime;

public interface PasswordLogService {
    void logNewUserPassword (User user, String rawPassword, LocalDateTime timestamp);

    void logLoginError (User user, String errorDescription, LocalDateTime timestamp);
}