package com.raven.calculator.controller;

import com.raven.calculator.dto.response.LoginResponse;
import com.raven.calculator.entity.User;
import com.raven.calculator.repository.UserRepository;
import com.raven.calculator.security.JwtUtil;
import com.raven.calculator.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTests {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthService authService;
    @MockBean
    AuthenticationManager authManager;
    @MockBean
    JwtUtil jwtUtil;
    @MockBean
    UserRepository userRepo;

    @Test
    void registerEndpoint_returns201() throws Exception {
        var json = """
      {"username":"u","email":"e@x","password":"p"}""";
        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        verify(authService).register(any());
    }

    @Test
    void loginEndpoint_returnsToken() throws Exception {
        String login = """
         {"username":"u","password":"p"}""";

        User userTest = new User();
        userTest.setUsername("u");
        userTest.setEmail("e");

        LoginResponse loginResponse = new LoginResponse("token", "u", "e");
        when(authService.login(any())).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.username").value("u"))
                .andExpect(jsonPath("$.email").value("e"));
    }

    @Test
    void loginEndpoint_withInvalidCredentials_returns401() throws Exception {
        String login = """
         {"username":"u","password":"p"}""";

        when(authService.login(any()))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"))
                .andExpect(jsonPath("$.details").value("Invalid username or password"));
    }

    @Test
    void loginEndpoint_withNonExistentUser_returns401() throws Exception {
        String login = """
         {"username":"nonexistent","password":"p"}""";

        when(authService.login(any()))
                .thenThrow(new BadCredentialsException("User not found"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"))
                .andExpect(jsonPath("$.details").value("User not found"));
    }
}