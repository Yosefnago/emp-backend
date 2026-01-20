package com.ms.sw.user.service;

import com.ms.sw.user.dto.UserRegisterRequest;
import com.ms.sw.user.model.User;
import com.ms.sw.exception.auth.EmailAlreadyExistsException;
import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.exception.auth.UserAlreadyExistsException;
import com.ms.sw.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    /**
     * Constructs the UserService, injecting the necessary dependencies.
     * <p>Uses constructor injection, the preferred Spring practice, for immutability and testability.
     *
     * @param userRepository The JPA repository for accessing user persistence data.
     * @param passwordEncoder The Spring Security component for encoding and verifying passwords.
     */
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
     * Authenticates a user by verifying the provided username and password.
     * <p>This method fetches the user by username and then securely compares the provided
     * plaintext password with the stored hashed password using {@link PasswordEncoder}.
     *
     * @param username The username attempting to log in.
     * @param password The plaintext password provided by the user.
     * @return An {@link Optional} containing the authenticated {@link User} entity.
     * @throws UsernameNotFoundException if no user is found with the given username.
     * @throws InvalidCredentialsException if the provided password does not match the stored hash.
     */
    public Optional<User> login(String username,String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return Optional.of(user);
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

}
