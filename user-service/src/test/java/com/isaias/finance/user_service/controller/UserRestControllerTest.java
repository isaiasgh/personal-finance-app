package com.isaias.finance.user_service.controller;

import com.isaias.finance.user_service.data.dto.*;
import com.isaias.finance.user_service.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserRestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterNewUser() throws Exception {
        UserRegistrationRequestDTO request = new UserRegistrationRequestDTO();
        UserBasicDTO response = new UserBasicDTO();

        request.setUsername("john.doe");
        request.setEmail("john@gmail.com");
        request.setName("John");
        request.setLastName("Doe");
        request.setPassword("123");

        response.setId(1);
        response.setUsername("john.doe");
        response.setEmail("john@gmail.com");
        response.setName("John");
        response.setLastName("Doe");

        when(userService.registerNewUser(request)).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john.doe"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));


    }

    @Test
    void testLoginUser() throws Exception {
        UserLoginRequestDTO request = new UserLoginRequestDTO();
        UserLoginResponseDTO response = new UserLoginResponseDTO();

        request.setUsername("john.doe");
        request.setPassword("123");

        response.setToken("fake-jwt-token");
        response.setTokenType("Bearer ");

        when(userService.loginUser(request)).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserPublicDTO user1 = new UserPublicDTO();
        user1.setId(1);
        user1.setUsername("alice");
        user1.setName("Alice");
        user1.setLastName("White");

        UserPublicDTO user2 = new UserPublicDTO();
        user2.setId(2);
        user2.setUsername("bob");
        user2.setName("Bob");
        user2.setLastName("Bobby");

        List<UserPublicDTO> users = List.of(
                user1,
                user2
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("alice"))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("White"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("bob"))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].lastName").value("Bobby"));
    }

    @Test
    void testGetUserInfo() throws Exception {
        UserBasicDTO user = new UserBasicDTO();

        user.setId(1);
        user.setUsername("john.doe");
        user.setEmail("john@gmail.com");
        user.setName("John");
        user.setLastName("Doe");

        when(userService.getUserInfo()).thenReturn(user);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john.doe"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testUpdatePassword() throws Exception {
        PasswordUpdateDTO dto = new PasswordUpdateDTO();
        dto.setCurrentPassword("oldPass");
        dto.setNewPassword("newPass");

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully."));
    }
}
