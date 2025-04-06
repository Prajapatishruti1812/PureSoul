package com.pure.soul.PureSoul.service;

import com.pure.soul.PureSoul.dto.LoginRequest;
import com.pure.soul.PureSoul.dto.LoginResponse;
import com.pure.soul.PureSoul.dto.RegisterRequest;
import com.pure.soul.PureSoul.entity.User;
import com.pure.soul.PureSoul.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse register(RegisterRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        loginResponse.setResponse("User registered successfully");
        return loginResponse;
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getLogin(), request.getLogin());
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        loginResponse.setResponse("Login successful");

        return loginResponse;
    }
}
