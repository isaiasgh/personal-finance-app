package com.isaias.finance.user_service.domain.service;

import com.isaias.finance.user_service.data.dto.PasswordUpdateDTO;
import com.isaias.finance.user_service.data.entity.User;

import java.time.LocalDateTime;

public interface AuthLogService {
    void logNewUserPassword (User user, String rawPassword, LocalDateTime timestamp);

    void logAuthError(User user, String errorDescription, LocalDateTime timestamp);

    void updatePassword(PasswordUpdateDTO dto, User user);
}