package com.ms.sw.user.service;

import com.ms.sw.user.dto.UserProfileDto;
import com.ms.sw.user.dto.UserRegisterRequest;
import com.ms.sw.user.model.User;
import com.ms.sw.exception.auth.EmailAlreadyExistsException;
import com.ms.sw.exception.auth.UserAlreadyExistsException;
import com.ms.sw.user.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * {@code UserService} is the primary service layer component responsible for all
 * business logic related to User management and Authentication flows.
 * <p>It orchestrates interactions between the {@link UserRepository} and security components
 * like {@link PasswordEncoder} to handle user registration, login verification,
 * and user data retrieval. It ensures security best practices, such as hashing passwords,
 * and handles application-specific exceptions for authentication failures.
 * @see UserRepository
 * @see org.springframework.security.crypto.password.PasswordEncoder
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a user entity based on their unique username.
     * <p>Used primarily by Spring Security's UserDetailsService interface for loading
     * user details during authentication or authorization checks.
     *
     * @param username The username (unique identifier) of the user to retrieve.
     * @return An {@link Optional} containing the {@link User} entity if found, or empty otherwise.
     */
    public Optional<User> getUser(String username) {

        return userRepository.findByUsername(username);
    }

    /**
     * Registers a new user account in the system.
     * <p>Before saving, it validates uniqueness for both username and email. The user's
     * password is **hashed** using {@link PasswordEncoder} before being persisted
     * for maximum security.
     *
     * @param userRegisterRequest The DTO containing the username, email, and password for registration.
     * @return The newly created and saved {@link User} entity.
     * @throws UserAlreadyExistsException if the provided username is already in use.
     * @throws EmailAlreadyExistsException if the provided email address is already associated with an account.
     */
    public User register(UserRegisterRequest userRegisterRequest) {

        if (userRepository.existsByUsername(userRegisterRequest.username())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(userRegisterRequest.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = new User();
        user.setUsername(userRegisterRequest.username());
        user.setPassword(passwordEncoder.encode(userRegisterRequest.password()));
        user.setEmail(userRegisterRequest.email());

        return userRepository.save(user);
    }

    public UserProfileDto getUserProfileDto(String username) {
        var user = userRepository.findByUsername(username).get();

        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getCompanyId(),
                user.getCompanyName(),
                user.getCompanyAddress()
        );
    }

}
