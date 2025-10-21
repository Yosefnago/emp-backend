package com.ms.sw.service;

import com.ms.sw.Dto.user.UserLogoutResponse;
import com.ms.sw.entity.User;
import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.exception.auth.UserAlreadyExistsException;
import com.ms.sw.exception.auth.UserNotFoundException;
import com.ms.sw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> login(String username,String password) {

        Optional<User> userReq = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(() -> (new UsernameNotFoundException("User not found"))));

        if (!passwordEncoder.matches(password, userReq.get().getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userReq;
    }

    public User register(String username,String email,String password) {

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        return userRepository.save(user);
    }


    public ResponseEntity<UserLogoutResponse> logout(String username) {

        SecurityContextHolder.clearContext();

         return ResponseEntity.ok(new UserLogoutResponse("User "+ username ," logged out successfully."));

    }
}
