package com.ms.sw.controller;


import com.ms.sw.Dto.user.*;
import com.ms.sw.entity.User;
import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.service.JwtService;
import com.ms.sw.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Auth", description = "ניהול התחברות והרשמה")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("auth")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        Optional<User> userOptional = userService.login(request.username(), request.password());

        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Login failed");
        }

        String token = jwtService.generateToken(userOptional.get().getUsername());
        return ResponseEntity.ok(new UserLoginResponse("Login successful", token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {

        userService.register(userRegisterRequest);
        return ResponseEntity.ok(new UserRegisterResponse("Register successful"));
    }


}
