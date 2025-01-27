package com.blogging.controller;

import com.blogging.DTO.PasswordUpdateRequestDTO;
import com.blogging.entity.Userr;
import com.blogging.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok) // Explicitly define the return type
                .orElseGet(() -> ResponseEntity.status(404).body("User not found")); // Return 404 with a String message
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Userr updatedUser) {
        try {
            Userr user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Handle user not found
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred while updating the profile."); // Handle other errors
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequestDTO request) {
        try {
            userService.updateUserPassword(id, request.getPassword());
            return ResponseEntity.ok("Password updated successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage()); // Handle validation errors
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred while updating the password."); // Handle other errors
        }
    }
}
