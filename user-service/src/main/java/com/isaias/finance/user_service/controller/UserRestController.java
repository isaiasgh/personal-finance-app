package com.isaias.finance.user_service.controller;

import com.isaias.finance.user_service.data.dto.*;
import com.isaias.finance.user_service.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @PostMapping ("/auth/register")
    public ResponseEntity<UserBasicDTO> registerNewUser (@RequestBody @Valid UserRegistrationRequestDTO userRegistrationRequestDTO) {
        return new ResponseEntity <> (userService.registerNewUser(userRegistrationRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping ("/auth/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser (@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {
        return new ResponseEntity <> (userService.loginUser(userLoginRequestDTO), HttpStatus.OK);
    }

    @GetMapping ("/users")
    public ResponseEntity<List<UserPublicDTO>> getAllUsers () {
        return new ResponseEntity <> (userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping ("/users/{id}")
    public ResponseEntity<UserPublicDTO> getUserById (@PathVariable @Valid Integer id) {
        return new ResponseEntity <> (userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping ("/users")
    public ResponseEntity<?> updatePassword (@RequestBody @Valid PasswordUpdateDTO dto) {
        userService.updatePassword(dto);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }
}