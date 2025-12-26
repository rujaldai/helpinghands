package com.helpinghands.service;

import com.helpinghands.dto.UserDTO;
import com.helpinghands.entity.User;
import com.helpinghands.exception.DuplicateResourceException;
import com.helpinghands.exception.ResourceNotFoundException;
import com.helpinghands.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO createGuestUser() {
        String guestId = generateUniqueGuestId();
        User guest = User.builder()
                .email("guest_" + guestId + "@temp.com")
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .name("Guest User")
                .role(User.Role.GUEST)
                .guestId(guestId)
                .active(true)
                .build();
        
        guest = userRepository.save(guest);
        return modelMapper.map(guest, UserDTO.class);
    }
    
    public UserDTO getUserByGuestId(String guestId) {
        return userRepository.findByGuestId(guestId)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null);
    }
    
    @Transactional
    public UserDTO convertGuestToUser(String guestId, String email, String password, String name) {
        User guest = userRepository.findByGuestId(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest user", guestId));
        
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email", email);
        }
        
        guest.setEmail(email);
        guest.setPassword(passwordEncoder.encode(password));
        guest.setName(name);
        guest.setRole(User.Role.DONATOR);
        
        guest = userRepository.save(guest);
        return modelMapper.map(guest, UserDTO.class);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }
    
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getActive() != null) {
            user.setActive(userDTO.getActive());
        }
        
        user = userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }
    
    private String generateUniqueGuestId() {
        String guestId;
        do {
            guestId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        } while (userRepository.existsByGuestId(guestId));
        return guestId;
    }
}

