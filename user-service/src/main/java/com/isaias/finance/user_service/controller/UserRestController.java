package com.isaias.finance.user_service.controller;

import com.isaias.finance.user_service.data.dto.UserBasicDTO;
import com.isaias.finance.user_service.data.dto.UserLoginRequestDTO;
import com.isaias.finance.user_service.data.dto.UserLoginResponseDTO;
import com.isaias.finance.user_service.data.dto.UserRegistrationRequestDTO;
import com.isaias.finance.user_service.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @PostMapping ("/auth/register")
    public ResponseEntity<UserBasicDTO> registerNewUser (@RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO) {
        return new ResponseEntity <> (userService.registerNewUser(userRegistrationRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping ("/auth/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser (@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        return new ResponseEntity <> (userService.loginUser(userLoginRequestDTO), HttpStatus.OK);
    }
}