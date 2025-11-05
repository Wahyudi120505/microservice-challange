package com.example.user_service.services.user;

import com.example.user_service.dto.AuthRequest;
import com.example.user_service.dto.AuthResponse;
import com.example.user_service.dto.Register;
import com.example.user_service.dto.UserResponse;

public interface UserService {
    Register register(Register request);
    AuthResponse login(AuthRequest request);
    UserResponse profile();
}
