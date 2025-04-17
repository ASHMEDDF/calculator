package com.raven.calculator.service;

import com.raven.calculator.dto.RegisterUserRequest;
import com.raven.calculator.entity.User;
import com.raven.calculator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    UserRepository userRepo;
    
    @Mock
    PasswordEncoder encoder;
    
    @InjectMocks
    AuthService authService;

    private RegisterUserRequest createValidRequest() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        return request;
    }

    @Test
    void registerSuccess() {
        // Given
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(request.getEmail())).thenReturn(false);
        when(encoder.encode(request.getPassword())).thenReturn("encoded_password");

        // When
        authService.register(request);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertEquals(request.getUsername(), savedUser.getUsername());
        assertEquals(request.getEmail(), savedUser.getEmail());
        assertEquals("encoded_password", savedUser.getPassword());
    }

    @Test
    void registerFailsWhenUsernameExists() {
        // Given
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Username already exists"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWhenEmailExists() {
        // Given
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(request.getEmail())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Email already exists"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWithInvalidEmail() {
        // Given
        RegisterUserRequest request = createValidRequest();
        request.setEmail("invalid-email");

        // Then
        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Invalid email format"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWithEmptyUsername() {
        // Given
        RegisterUserRequest request = createValidRequest();
        request.setUsername("");

        // Then
        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Username cannot be empty"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWithEmptyPassword() {
        // Given
        RegisterUserRequest request = createValidRequest();
        request.setPassword("");

        // Then
        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Password cannot be empty"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void passwordIsProperlyEncoded() {
        // Given
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(request.getEmail())).thenReturn(false);
        when(encoder.encode(request.getPassword())).thenReturn("encoded_password_123");

        // When
        authService.register(request);

        // Then
        verify(userRepo).save(argThat(user -> 
            user.getPassword().equals("encoded_password_123")
        ));
    }
}
