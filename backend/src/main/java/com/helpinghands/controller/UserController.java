package com.helpinghands.controller;

import com.helpinghands.dto.RegisterRequest;
import com.helpinghands.dto.UserDTO;
import com.helpinghands.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    
    @PostMapping("/guest")
    public ResponseEntity<UserDTO> createGuestUser() {
        logger.info("Creating new guest user");
        UserDTO guestUser = userService.createGuestUser();
        logger.info("Successfully created guest user with ID: {}", guestUser.getId());
        return ResponseEntity.ok(guestUser);
    }
    
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<UserDTO> getGuestUser(@PathVariable String guestId) {
        logger.debug("Fetching guest user with ID: {}", guestId);
        UserDTO user = userService.getUserByGuestId(guestId);
        if (user == null) {
            logger.warn("Guest user not found with ID: {}", guestId);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Found guest user: {}", user.getEmail());
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/guest/{guestId}/convert")
    public ResponseEntity<UserDTO> convertGuestToUser(
            @PathVariable String guestId,
            @RequestBody RegisterRequest request) {
        logger.info("Converting guest user with ID: {} to registered user", guestId);
        UserDTO user = userService.convertGuestToUser(
                guestId, request.getEmail(), request.getPassword(), request.getName());
        logger.info("Successfully converted guest to registered user with email: {}", request.getEmail());
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Admin fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        logger.debug("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        logger.info("Admin updating user with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        logger.info("Successfully updated user with ID: {}", id);
        return ResponseEntity.ok(updatedUser);
    }
}

