package com.helpinghands.service;

import com.helpinghands.dto.AuthRequest;
import com.helpinghands.dto.AuthResponse;
import com.helpinghands.dto.RegisterRequest;
import com.helpinghands.dto.UserDTO;
import com.helpinghands.entity.User;
import com.helpinghands.repository.UserRepository;
import com.helpinghands.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if (!user.getActive()) {
            throw new RuntimeException("User account is inactive");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        
        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(User.Role.DONATOR)
                .active(true)
                .build();
        
        // If guestId is provided, link guest donations to this user
        if (request.getGuestId() != null && !request.getGuestId().isEmpty()) {
            final User finalUser = user;
            userRepository.findByGuestId(request.getGuestId())
                    .ifPresent(guest -> {
                        // Transfer donations from guest to new user
                        guest.getDonations().forEach(donation -> {
                            donation.setUser(finalUser);
                        });
                        userRepository.delete(guest);
                    });
        }
        
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        
        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }
}

