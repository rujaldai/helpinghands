package com.helpinghands.service;

import com.helpinghands.dto.AuthRequest;
import com.helpinghands.dto.AuthResponse;
import com.helpinghands.dto.RegisterRequest;
import com.helpinghands.dto.UserDTO;
import com.helpinghands.entity.User;
import com.helpinghands.exception.DuplicateResourceException;
import com.helpinghands.exception.InvalidCredentialsException;
import com.helpinghands.exception.InvalidOperationException;
import com.helpinghands.repository.DonationRepository;
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
    private final DonationRepository donationRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        
        if (!user.isActive()) {
            throw new InvalidOperationException("User account is inactive");
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
            throw new DuplicateResourceException("Email", request.getEmail());
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(User.Role.DONATOR)
                .active(true)
                .build();
        
        // Save user first
        user = userRepository.save(user);
        final User finalUser = user;
        // If guestId is provided, link guest donations to this user
        if (request.getGuestId() != null && !request.getGuestId().isEmpty()) {
            userRepository.findByGuestId(request.getGuestId())
                    .ifPresent(guest -> {
                        // Transfer donations from guest to new user
                        guest.getDonations().forEach(donation -> {
                            donation.setUser(finalUser);
                            donationRepository.save(donation);
                        });
                        // Delete guest user after transferring donations
                        userRepository.delete(guest);
                    });
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        
        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }
}

