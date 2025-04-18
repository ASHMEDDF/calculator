package com.raven.calculator.controller;

import com.raven.calculator.dto.LoginRequest;
import com.raven.calculator.dto.RegisterUserRequest;
import com.raven.calculator.dto.response.ErrorResponse;
import com.raven.calculator.dto.response.LoginResponse;
import com.raven.calculator.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Authentication", description = "Registro y login de usuarios")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Registrar usuario",
            description = "Crea un nuevo usuario con username, email y password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de registro",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación de los datos")
            }
    )
    public ResponseEntity<String> register(@Valid @jakarta.validation.constraints.NotNull RegisterUserRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida credenciales y devuelve JWT",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales de login",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = LoginRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = LoginResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> login(@Valid @jakarta.validation.constraints.NotNull LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Unauthorized",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
