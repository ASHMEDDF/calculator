package com.raven.calculator.service;

import com.raven.calculator.dto.LoginRequest;
import com.raven.calculator.dto.RegisterUserRequest;
import com.raven.calculator.dto.response.LoginResponse;
import com.raven.calculator.entity.User;
import com.raven.calculator.repository.UserRepository;
import com.raven.calculator.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      AuthenticationManager authManager,
                      JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterUserRequest request) {
        validateRequest(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtUtil.generateToken(auth);

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            return new LoginResponse(token, user.getUsername(), user.getEmail());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private void validateRequest(RegisterUserRequest request) {
        if (!StringUtils.hasText(request.getUsername())) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (!StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (!StringUtils.hasText(request.getEmail())) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
