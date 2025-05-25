package com.isaias.finance.user_service.controller;

import com.isaias.finance.user_service.data.dto.*;
import com.isaias.finance.user_service.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.isaias.finance.user_service.config.OpenApiConfig.SECURITY_SCHEME_NAME;

@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "Operations related to users authentication and management")
public class UserRestController {
    private final UserService userService;

    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserBasicDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping ("/auth/register")
    public ResponseEntity<UserBasicDTO> registerNewUser (@RequestBody @Valid UserRegistrationRequestDTO userRegistrationRequestDTO) {
        return new ResponseEntity <> (userService.registerNewUser(userRegistrationRequestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "User login", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = UserLoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials")
    })
    @PostMapping ("/auth/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser (@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {
        return new ResponseEntity <> (userService.loginUser(userLoginRequestDTO), HttpStatus.OK);
    }

    @Operation(summary = "Get all users (public info)", responses = {
            @ApiResponse(responseCode = "200", description = "List of all users",
                    content = @Content(schema = @Schema(implementation = UserPublicDTO.class)))
    })
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping ("/users/all")
    public ResponseEntity<List<UserPublicDTO>> getAllUsers () {
        return new ResponseEntity <> (userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Get current logged in user info", responses = {
            @ApiResponse(responseCode = "200", description = "User info",
                    content = @Content(schema = @Schema(implementation = UserBasicDTO.class)))
    })
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping ("/users")
    public UserBasicDTO getUserInfo () {
        return userService.getUserInfo();
    }

    @Operation(summary = "Update user password", responses = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping ("/users")
    public ResponseEntity<?> updatePassword (@RequestBody @Valid PasswordUpdateDTO dto) {
        userService.updatePassword(dto);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }

    @Operation(summary = "Check if username is valid", responses = {
            @ApiResponse(responseCode = "200", description = "Returns true if username is valid, false otherwise")
    })
    @GetMapping("/users/{username}")
    public boolean isUsernameValid (@PathVariable String username) {
        return userService.isUsernameValid(username);
    }
}