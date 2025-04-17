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
        
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(request.getEmail())).thenReturn(false);
        when(encoder.encode(request.getPassword())).thenReturn("encoded_password");

        authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertEquals(request.getUsername(), savedUser.getUsername());
        assertEquals(request.getEmail(), savedUser.getEmail());
        assertEquals("encoded_password", savedUser.getPassword());
    }

    @Test
    void registerFailsWhenUsernameExists() {
        
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Username already exists"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWhenEmailExists() {
        
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Email already exists"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWithInvalidEmail() {
        
        RegisterUserRequest request = createValidRequest();
        request.setEmail("invalid-email");

        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Invalid email format"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWithEmptyUsername() {
        
        RegisterUserRequest request = createValidRequest();
        request.setUsername("");

        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Username cannot be empty"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerFailsWithEmptyPassword() {
        
        RegisterUserRequest request = createValidRequest();
        request.setPassword("");
        
        assertThrows(IllegalArgumentException.class, 
            () -> authService.register(request),
            "Password cannot be empty"
        );
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void passwordIsProperlyEncoded() {
        
        RegisterUserRequest request = createValidRequest();
        when(userRepo.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(request.getEmail())).thenReturn(false);
        when(encoder.encode(request.getPassword())).thenReturn("encoded_password_123");

        authService.register(request);
        
        verify(userRepo).save(argThat(user -> 
            user.getPassword().equals("encoded_password_123")
        ));
    }
}
