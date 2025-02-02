package com.blogging.controller;

import com.blogging.DTO.LoginRequest;
import com.blogging.DTO.RegisterRequestDTO;
import com.blogging.config.BeanLoader;
import com.blogging.entity.AppUser;
import com.blogging.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;
    private final BeanLoader jwtTokenUtil;

    public LoginController(UserService userService, BeanLoader jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    // Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Validated RegisterRequestDTO registerRequestDTO) {
        userService.registerUser(registerRequestDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Validated LoginRequest loginRequest) {
        AppUser user = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());

        String token = jwtTokenUtil.generateToken(user.getEmail());

        // Returning structured response
        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName()
        ));
    }
}
