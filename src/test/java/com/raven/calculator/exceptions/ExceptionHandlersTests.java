package com.raven.calculator.exceptions;

import com.raven.calculator.config.SecurityConfig;
import com.raven.calculator.config.exception.DivisionByZeroException;
import com.raven.calculator.controller.CalculateController;
import com.raven.calculator.security.CustomUserDetailsService;
import com.raven.calculator.security.JwtAuthenticationEntryPoint;
import com.raven.calculator.security.JwtAuthenticationFilter;
import com.raven.calculator.security.JwtUtil;
import com.raven.calculator.service.CalculateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculateController.class)
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        CustomUserDetailsService.class
})
class ExceptionHandlersTests {

    @Autowired
    MockMvc mockMvc;
    
    @MockBean CalculateService calcService;
    @MockBean JwtUtil jwtUtil;
    @MockBean CustomUserDetailsService customUserDetailsService;
    @MockBean JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @BeforeEach
    void setup() {
        when(jwtUtil.validateToken("faketok")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("faketok")).thenReturn("testuser");
        when(customUserDetailsService.loadUserByUsername("testuser"))
                .thenReturn(User.withUsername("testuser")
                        .password("irrelevant")
                        .authorities("ROLE_USER")
                        .build());
    }

    @Test
    void divisionByZeroHandled() throws Exception {
        when(calcService.calculate(any()))
                .thenThrow(new DivisionByZeroException());
        mockMvc.perform(post("/api/calculate")
                        .header("Authorization", "Bearer faketok")
                        .contentType(APPLICATION_JSON)
                        .content("""
               {"operation":"DIVISION","operandA":5,"operandB":0}
             """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Division by zero is not allowed"));
    }

    @Test
    void beanValidationErrorsMapped() throws Exception {
        String bad = """
        {"operandA":1000001}""";

        mockMvc.perform(post("/api/calculate")
                        .header("Authorization", "Bearer faketok")
                        .contentType(APPLICATION_JSON)
                        .content(bad))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists());
    }
}