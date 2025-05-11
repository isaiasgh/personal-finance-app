package com.isaias.finance.user_service.domain.service.impl;

import com.isaias.finance.user_service.data.entity.PasswordLog;
import com.isaias.finance.user_service.data.entity.User;
import com.isaias.finance.user_service.data.repository.PasswordLogRepository;
import com.isaias.finance.user_service.domain.service.AuthLogServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthLogServiceImplTest {
    @Mock
    private PasswordEncoder encoder;

    @Mock
    private PasswordLogRepository passwordLogRepository;

    @InjectMocks
    private AuthLogServiceImpl subject;

    private User user;
    private PasswordLog passwordLog;
    private String rawPassword;
    private LocalDateTime timestamp;

    @BeforeEach
    void setUp () {
        String name = "John";
        String lastName = "Doe";
        String email = "john@gmail.com";
        String username = "john.doe";
        rawPassword = "123";
        Integer id = 1;
        timestamp = LocalDateTime.now();

        user = new User();
        user.setId(id);
        user.setName(name);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);

        passwordLog = new PasswordLog();
        passwordLog.setUser(user);
        passwordLog.setAttemptTimestamp(timestamp);
    }

    @Test
    void shouldLogNewPasswordWhenIsSuccessful () {
        passwordLog.setHashedPassword("password is encoded");
        when(encoder.encode(rawPassword)).thenReturn("password is encoded");

        subject.logNewUserPassword(user, rawPassword, timestamp);

        ArgumentCaptor<PasswordLog> captor = ArgumentCaptor.forClass(PasswordLog.class);
        verify(passwordLogRepository, times(1)).save(captor.capture());

        PasswordLog savedLog = captor.getValue();
        assertEquals(user, savedLog.getUser());
        assertEquals("password is encoded", savedLog.getHashedPassword());
        assertEquals(timestamp, savedLog.getAttemptTimestamp());
    }
}