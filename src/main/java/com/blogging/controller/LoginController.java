package com.blogging.controller;

import com.blogging.DTO.LoginRequest;
import com.blogging.DTO.RegisterRequestDTO;
import com.blogging.config.JwtUtil;
import com.blogging.entity.AppUser;
import com.blogging.enums.Role;
import com.blogging.repository.UserRepository;
import com.blogging.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginController(UserService userService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        AppUser user = userService.findByEmail(loginRequest.getEmail().trim().toLowerCase());
        return jwtUtil.generateToken(user.getEmail().trim().toLowerCase(), user.getRoles().toString(), user.getFirstName(), user.getLastName(), user.getAuthType(), user.getId()); // Send token to the client
    }


    // Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Validated RegisterRequestDTO registerRequestDTO) {
        userService.registerUser(registerRequestDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/loginViaGoogle")
    public String loginViaGoogle(@RequestBody LoginRequest loginRequest) {

        AppUser user = userService.findByEmail(loginRequest.getEmail());

        if (user == null) {
            AppUser newUser = new AppUser();
            newUser.setFirstName(loginRequest.getFirstName());
            newUser.setLastName(loginRequest.getLastName());
            newUser.setEmail(loginRequest.getEmail());
            newUser.setPassword(new BCryptPasswordEncoder().encode("James@111"));
            newUser.setAuthType(loginRequest.getAuthType());
            newUser.setRoles(Set.of(Role.USER.name()));

            user = userRepository.save(newUser);
        }

        return jwtUtil.generateToken(
                user.getEmail(),
                user.getRoles().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getAuthType(),
                user.getId()
        );

    }

}
