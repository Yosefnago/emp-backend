package com.ms.sw.user.controller;


import com.dev.tools.Markers.ApiMarker;
import com.ms.sw.user.model.User;
import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.config.service.JwtService;
import com.ms.sw.user.dto.UserLoginRequest;
import com.ms.sw.user.dto.UserLoginResponse;
import com.ms.sw.user.dto.UserRegisterRequest;
import com.ms.sw.user.dto.UserRegisterResponse;
import com.ms.sw.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("auth")
@ApiMarker
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        System.out.println("Logout successful");
        return ResponseEntity.noContent().build();
    }

}
