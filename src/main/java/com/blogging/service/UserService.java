package com.blogging.service;

import com.blogging.DTO.RegisterRequestDTO;
import com.blogging.entity.Userr;
import com.blogging.exception.ResourceNotFoundException;
import com.blogging.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    // Fetch user by ID
    public Optional<Userr> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Update user details
    public Userr updateUser(Long id, Userr updatedUser) {
        Userr user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());

        // Update password if provided and non-empty
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }

    // Register a new user
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use");
        }

        // Build a new Userr object
        Userr user = buildUserFromDTO(registerRequestDTO);

        // Save to the database
        userRepository.save(user);
    }

    // Validate user credentials
    public Userr validateUser(String email, String password) {
        Userr user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }

    // Update user password
    public void updateUserPassword(Long userId, String newPassword) {
        Userr user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Encode and save the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Private utility method to build an Userr object from a DTO
    private Userr buildUserFromDTO(RegisterRequestDTO registerRequestDTO) {
        Userr user = new Userr();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword())); // Hash the password
        return user;
    }
}
