package com.raven.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class RegisterUserRequest {
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
