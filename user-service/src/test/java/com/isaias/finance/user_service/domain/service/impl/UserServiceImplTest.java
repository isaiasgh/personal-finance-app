package com.isaias.finance.user_service.domain.service.impl;

import com.isaias.finance.user_service.data.dto.PasswordUpdateDTO;
import com.isaias.finance.user_service.data.dto.UserBasicDTO;
import com.isaias.finance.user_service.data.dto.UserPublicDTO;
import com.isaias.finance.user_service.data.dto.UserRegistrationRequestDTO;
import com.isaias.finance.user_service.data.entity.User;
import com.isaias.finance.user_service.data.mapper.UserMapper;
import com.isaias.finance.user_service.data.repository.UserRepository;
import com.isaias.finance.user_service.domain.exception.UserAlreadyExistsException;
import com.isaias.finance.user_service.domain.service.AuthLogService;
import com.isaias.finance.user_service.domain.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthLogService authLogService;

    @InjectMocks
    private UserServiceImpl subject;

    private UserRegistrationRequestDTO newUser;
    private UserBasicDTO userResponse;
    private User user;
    private UserPublicDTO userPublicDTO;
    private PasswordUpdateDTO passwordUpdateDTO;

    @BeforeEach
    void setUp() {
        String name = "John";
        String lastName = "Doe";
        String email = "john@gmail.com";
        String username = "john.doe";
        String password = "1234";
        Integer id = 1;

        newUser = new UserRegistrationRequestDTO();
        newUser.setName(name);
        newUser.setLastName(lastName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);

        user = new User ();
        user.setId(id);
        user.setName(name);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);

        userResponse = new UserBasicDTO();
        userResponse.setId(id);
        userResponse.setName(name);
        userResponse.setLastName(lastName);
        userResponse.setUsername(username);
        userResponse.setEmail(email);

        userPublicDTO = new UserPublicDTO();
        userPublicDTO.setId(id);
        userPublicDTO.setName(name);
        userPublicDTO.setLastName(lastName);
        userPublicDTO.setUsername(username);

        passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setCurrentPassword(password);
        passwordUpdateDTO.setNewPassword("1234");
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

    @DisplayName("Should return UserBasicDTO when method is successful")
    @Test
    void shouldReturnUserBasicDTOWhenMethodIsSuccessful () {
        when (userRepository.existsUserByEmail("john@gmail.com")).thenReturn(false);
        when (userRepository.existsUserByUsername("john.doe")).thenReturn(false);

        when (userMapper.userRegistrationRequestDTOToUser(newUser)).thenReturn(user);
        when (userMapper.userToUserBasicDTO(user)).thenReturn(userResponse);

        assertEquals(userResponse, subject.registerNewUser(newUser));

        verify(userRepository, times(1)).save(user);
        verify(authLogService, times(1)).logNewUserPassword(eq(user), eq(newUser.getPassword()), any());
    }

    @DisplayName("Should return a list of UserPublicDTOs when users are found")
    @Test
    void shouldReturnListOfUserPublicDTOsWhenUsersExist () {
        when (userRepository.findAll()).thenReturn(List.of(user));

        when (userMapper.userToUserPublicDTO(user)).thenReturn(userPublicDTO);

        assertEquals(List.of(userPublicDTO), subject.getAllUsers());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).userToUserPublicDTO(user);
    }

    @DisplayName("Should update user password when authenticated user is found")
    @Test
    void shouldUpdatePassword () {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("john.doe");
        SecurityContextHolder.setContext(securityContext);

        when (userRepository.findByUsername("john.doe")).thenReturn(Optional.of(user));

        subject.updatePassword(passwordUpdateDTO);

        verify(authLogService, times(1)).updatePassword(passwordUpdateDTO, user);
    }
}