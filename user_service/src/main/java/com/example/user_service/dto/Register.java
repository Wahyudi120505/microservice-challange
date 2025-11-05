package com.example.user_service.dto;

import com.example.user_service.enums.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    private String email;
    private String password;
    private Roles role;
}
