package com.example.user_service.services.user;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_service.dto.AuthRequest;
import com.example.user_service.dto.AuthResponse;
import com.example.user_service.dto.Register;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.jwt.JwtUtil;
import com.example.user_service.models.User;
import com.example.user_service.models.UserToken;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.repository.UserTokenRepository;
import com.example.user_service.services.mail.MailService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private MailService emailService;

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    @Override
    public Register register(Register request) {
        Locale locale = Locale.getDefault();
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            User newUser = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();
            userRepository.save(newUser);

            emailService.emailRegistration(request.getEmail(), request.getEmail());
            return request;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                getMessage("user.register.email.exists", locale));
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Locale locale = Locale.getDefault();
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        getMessage("user.login.invalid", locale)));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    getMessage("user.login.invalid", locale));
        }

        UserToken refreshTokenEntity = userTokenRepository.findByUserAndExpiredFalse(user).orElse(null);
        String refreshToken;
        boolean createNewRefresh = false;

        if (refreshTokenEntity == null) {
            createNewRefresh = true;
        } else {
            if (jwtUtil.isTokenExpired(refreshTokenEntity.getRefreshToken())
                    || !refreshTokenEntity.getUser().getPassword().equals(user.getPassword())
                    || !refreshTokenEntity.getUser().getRole().equals(user.getRole())) {
                refreshTokenEntity.setExpired(true);
                refreshTokenEntity.setRevoked(true);
                userTokenRepository.save(refreshTokenEntity);
                createNewRefresh = true;
            }
        }

        if (createNewRefresh) {
            refreshToken = jwtUtil.generateRefreshToken(user);
            UserToken newToken = UserToken.builder()
                    .refreshToken(refreshToken)
                    .user(user)
                    .expired(false)
                    .revoked(false)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build();
            userTokenRepository.save(newToken);
        } else {
            refreshToken = refreshTokenEntity.getRefreshToken();
        }

        String accessToken = jwtUtil.generateAccessToken(user);

        return new AuthResponse(user.getEmail(), user.getRole().name(), accessToken, refreshToken);
    }

    @Override
    public UserResponse profile() {
        Locale locale = Locale.getDefault();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, getMessage("user.unauthorized", locale));
        }

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        getMessage("user.notfound", locale)));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
