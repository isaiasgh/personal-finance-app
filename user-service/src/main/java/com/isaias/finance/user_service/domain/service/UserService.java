package com.isaias.finance.user_service.domain.service;

import com.isaias.finance.user_service.data.dto.*;

import java.util.List;

public interface UserService {
    UserBasicDTO registerNewUser (UserRegistrationRequestDTO userRegistrationRequestDTO);

    UserLoginResponseDTO loginUser (UserLoginRequestDTO userLoginRequestDTO);

    List <UserPublicDTO> getAllUsers ();

    UserPublicDTO getUserById (Integer id);

    void updatePassword (PasswordUpdateDTO dto);
}