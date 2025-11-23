package com.ms.sw.service;

import com.ms.sw.entity.User;
import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.exception.auth.UserAlreadyExistsException;
import com.ms.sw.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
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

        log.info("UserService: login {}", username);
        Optional<User> userReq = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(() -> (new UsernameNotFoundException("User not found"))));

        if (!passwordEncoder.matches(password, userReq.get().getPassword())) {
            log.debug("UserService: login failed {}", username);
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userReq;
    }

    public User register(String username,String email,String password) {
        log.info("UserService: register {}", username);

        if (userRepository.existsByUsername(username)) {
            log.debug("UserService: register failed existing username {}", username);
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            log.debug("UserService: register failed email exists{}", username);
            throw new UserAlreadyExistsException("Email already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        log.info("UserService: register user success{}", username);
        return userRepository.save(user);
    }

}
