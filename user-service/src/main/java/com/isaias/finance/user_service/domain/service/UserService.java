package com.isaias.finance.user_service.domain.service;

import com.isaias.finance.user_service.data.dto.UserBasicDTO;
import com.isaias.finance.user_service.data.dto.UserLoginRequestDTO;
import com.isaias.finance.user_service.data.dto.UserLoginResponseDTO;
import com.isaias.finance.user_service.data.dto.UserRegistrationRequestDTO;

public interface UserService {
    UserBasicDTO registerNewUser (UserRegistrationRequestDTO userRegistrationRequestDTO);

    UserLoginResponseDTO loginUser (UserLoginRequestDTO userLoginRequestDTO);
}