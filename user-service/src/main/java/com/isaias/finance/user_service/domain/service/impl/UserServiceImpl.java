package com.isaias.finance.user_service.domain.service.impl;

import com.isaias.finance.user_service.config.security.JwtProvider;
import com.isaias.finance.user_service.data.dto.*;
import com.isaias.finance.user_service.data.entity.User;
import com.isaias.finance.user_service.data.mapper.UserMapper;
import com.isaias.finance.user_service.data.repository.UserRepository;
import com.isaias.finance.user_service.domain.exception.UserAlreadyExistsException;
import com.isaias.finance.user_service.domain.service.AuthLogService;
import com.isaias.finance.user_service.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private final AuthLogService authLogService;

    @Override
    public UserBasicDTO registerNewUser(UserRegistrationRequestDTO userRequest) {
        boolean credentialsAreAvailable = areUserCredentialsAvailable (userRequest);

        if (!credentialsAreAvailable) {
            throw new UserAlreadyExistsException("User credentials are already in use");
        }

        LocalDateTime creationTime = LocalDateTime.now();

        User user = userMapper.userRegistrationRequestDTOToUser(userRequest);
        user.setCreatedAt(creationTime);
        userRepository.save(user);

        authLogService.logNewUserPassword(user, userRequest.getPassword(), creationTime);

        return userMapper.userToUserBasicDTO(user);
    }

    @Override
    public UserLoginResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userLoginRequestDTO.getUsername(),
                    userLoginRequestDTO.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            User user = userRepository.findByUsername(userLoginRequestDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));


            UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
            userLoginResponseDTO.setToken(token);

            return userLoginResponseDTO;

        } catch (BadCredentialsException ex) {
            userRepository.findByUsername(userLoginRequestDTO.getUsername())
                    .ifPresent(user -> authLogService.logAuthError(user, "Incorrect password.", LocalDateTime.now()));

            throw ex;
        }
    }

    @Override
    public List<UserPublicDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserPublicDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserBasicDTO getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return userMapper.userToUserBasicDTO(user);
    }

    @Override
    public void updatePassword (PasswordUpdateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        authLogService.updatePassword(dto, user);
    }

    @Override
    public boolean isUsernameValid(String username) {
        return userRepository.existsUserByUsername(username);
    }

    private boolean areUserCredentialsAvailable (UserRegistrationRequestDTO userRequest) {
        boolean usernameIsAvailable = !userRepository.existsUserByUsername(userRequest.getUsername());
        boolean emailIsAvailable = !userRepository.existsUserByEmail(userRequest.getEmail());

        return usernameIsAvailable && emailIsAvailable;
    }
}