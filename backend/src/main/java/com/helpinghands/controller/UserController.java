package com.helpinghands.controller;

import com.helpinghands.dto.RegisterRequest;
import com.helpinghands.dto.UserDTO;
import com.helpinghands.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/guest")
    public ResponseEntity<UserDTO> createGuestUser() {
        return ResponseEntity.ok(userService.createGuestUser());
    }
    
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<UserDTO> getGuestUser(@PathVariable String guestId) {
        UserDTO user = userService.getUserByGuestId(guestId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/guest/{guestId}/convert")
    public ResponseEntity<UserDTO> convertGuestToUser(
            @PathVariable String guestId,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.convertGuestToUser(
                guestId, request.getEmail(), request.getPassword(), request.getName()));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }
}

