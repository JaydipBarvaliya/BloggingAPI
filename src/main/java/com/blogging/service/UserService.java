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

    public Optional<Userr> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Userr updateUser(Long id, Userr updatedUser) {
        Userr user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());

        if (isValidPassword(updatedUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }

    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use");
        }

        Userr user = buildUserFromDTO(registerRequestDTO);
        userRepository.save(user);
    }

    public Userr validateUser(String email, String password) {
        Userr user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }

    public void updateUserPassword(Long userId, String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        Userr user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private Userr buildUserFromDTO(RegisterRequestDTO registerRequestDTO) {
        Userr user = new Userr();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword())); // Hash the password
        return user;
    }

    private boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty();
    }
}
