package com.example.user_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_service.dto.AuthRequest;
import com.example.user_service.dto.GenericResponse;
import com.example.user_service.dto.Register;
import com.example.user_service.services.user.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Register request) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(userService.register(request),
                    "Successfully Registration"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(userService.login(request),
                    "Successfully Login"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    

    @GetMapping("/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> profile() {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(userService.profile(),
                    "Successfully Get Profile"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

}
