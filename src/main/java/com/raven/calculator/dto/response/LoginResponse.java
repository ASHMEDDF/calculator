package com.raven.calculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private final String token;
    private final String tokenType = "Bearer";
    private final String username;
    private final String email;
}
