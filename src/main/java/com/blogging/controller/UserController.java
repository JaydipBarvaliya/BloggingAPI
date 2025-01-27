package com.blogging.controller;

import com.blogging.DTO.PasswordUpdateRequestDTO;
import com.blogging.entity.Userr;
import com.blogging.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        Optional<Userr> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get()); // Return the Userr object if found
        } else {
            return ResponseEntity.status(404).body("User not found"); // Return a 404 status with a String message
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Userr updatedUser) {
        try {
            Userr user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the profile.");
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequestDTO request) {
        try {
            userService.updateUserPassword(id, request.getPassword());
            return ResponseEntity.ok("Password updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
