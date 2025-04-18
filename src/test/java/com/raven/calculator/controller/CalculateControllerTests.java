package com.raven.calculator.controller;

import com.raven.calculator.config.SecurityConfig;
import com.raven.calculator.entity.OperationEntity;
import com.raven.calculator.security.CustomUserDetailsService;
import com.raven.calculator.security.JwtAuthenticationEntryPoint;
import com.raven.calculator.security.JwtAuthenticationFilter;
import com.raven.calculator.security.JwtUtil;
import com.raven.calculator.service.CalculateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static com.raven.calculator.entity.OperationTypeEnum.ADDITION;
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
@AutoConfigureMockMvc(addFilters = true)
class CalculateControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean JwtUtil jwtUtil;
    @MockBean CalculateService calcService;
    @MockBean CustomUserDetailsService customUserDetailsService;
    @MockBean JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    void calculate_withValidToken_returns200() throws Exception {
        String req = """
      {"operation":"ADDITION","operandA":5,"operandB":3}
    """;

        OperationEntity op = new OperationEntity();
        op.setId(1L);
        op.setOperationType(ADDITION);
        op.setOperandA(BigDecimal.valueOf(5));
        op.setOperandB(BigDecimal.valueOf(3));
        op.setResult(BigDecimal.valueOf(8));
        op.setTimestamp(Instant.now());
        op.setUserId(1L);
        when(calcService.calculate(any())).thenReturn(op);

        when(jwtUtil.validateToken("fake")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("fake")).thenReturn("testuser");
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();
        when(customUserDetailsService.loadUserByUsername("testuser")).thenReturn(ud);

        mockMvc.perform(post("/api/calculate")
                        .header("Authorization", "Bearer fake")
                        .contentType(APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(8));
    }

    @Test
    void calculate_withoutToken_returns401() throws Exception {
        mockMvc.perform(post("/api/calculate")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"))
                .andExpect(jsonPath("$.details").value("Token is required"));
    }

    @Test
    void calculate_withInvalidToken_returns401() throws Exception {
        when(jwtUtil.validateToken("invalid")).thenReturn(false);

        mockMvc.perform(post("/api/calculate")
                        .header("Authorization", "Bearer invalid")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"))
                .andExpect(jsonPath("$.details").value("Token is invalid or expired"));
    }

    @Test
    void calculate_withExpiredToken_returns401() throws Exception {
        when(jwtUtil.validateToken("expired")).thenReturn(false);

        mockMvc.perform(post("/api/calculate")
                        .header("Authorization", "Bearer expired")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"))
                .andExpect(jsonPath("$.details").value("Token is invalid or expired"));
    }
}