package com.blogging.service;

import com.blogging.DTO.RegisterRequestDTO;
import com.blogging.entity.Userr;
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

    public Optional<Userr> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Userr updateUser(Long id, Userr updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());

            // Only update password if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public void registerUser(RegisterRequestDTO registerRequestDTO) {

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        // Create a new Userr object and save to the database
        Userr user = new Userr();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }
    public Userr validateUser(String email, String password) {
        Userr user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }

    public void updateUserPassword(Long userId, String newPassword) {
        Userr user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
