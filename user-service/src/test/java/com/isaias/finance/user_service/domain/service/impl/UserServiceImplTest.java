package com.isaias.finance.user_service.domain.service.impl;

import com.isaias.finance.user_service.data.dto.UserRegistrationRequestDTO;
import com.isaias.finance.user_service.data.repository.UserRepository;
import com.isaias.finance.user_service.domain.exception.UserAlreadyExistsException;
import com.isaias.finance.user_service.domain.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl subject;

    private UserRegistrationRequestDTO newUser;

    @BeforeEach
    void setUp() {
        newUser = new UserRegistrationRequestDTO();
        newUser.setName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john@gmail.com");
        newUser.setUsername("john.doe");
        newUser.setPassword("1234");
    }

    @DisplayName("Should throw exception when user credentials already exist")
    @Test
    void shouldThrowExceptionWhenCredentialsExist () {
        when (userRepository.existsUserByEmail("john@gmail.com")).thenReturn(true);
        when (userRepository.existsUserByUsername("john.doe")).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> subject.registerNewUser(newUser));

        assertEquals("User credentials are already in use", ex.getMessage());

        verify(userRepository, never()).save(any());
    }
}