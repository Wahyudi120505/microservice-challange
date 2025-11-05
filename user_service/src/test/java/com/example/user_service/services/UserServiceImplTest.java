package com.example.user_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_service.dto.Register;
import com.example.user_service.jwt.JwtUtil;
import com.example.user_service.models.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.repository.UserTokenRepository;
import com.example.user_service.services.mail.MailService;
import com.example.user_service.services.user.UserServiceImpl;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private MailService emailService;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewUser_Success() {
        Register request = new Register();
        request.setEmail("test@test.com");
        request.setPassword("password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Email sudah terdaftar");

        Register result = userService.register(request);
        assertNotNull(result);
        assertEquals(request.getEmail(), result.getEmail());
    }

    @Test
    void testRegisterExistingUser_ThrowsException() {
        Register request = new Register();
        request.setEmail("test@test.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Email sudah terdaftar");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.register(request);
        });

        assertEquals("Email sudah terdaftar", exception.getReason());
    }
}