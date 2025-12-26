package com.helpinghands.service;

import com.helpinghands.dto.StatementDTO;
import com.helpinghands.dto.StatementRequest;
import com.helpinghands.entity.Donation;
import com.helpinghands.entity.Statement;
import com.helpinghands.entity.User;
import com.helpinghands.repository.DonationRepository;
import com.helpinghands.repository.StatementRepository;
import com.helpinghands.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatementService {
    
    private final StatementRepository statementRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    @Transactional
    public StatementDTO createStatement(StatementRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Donation donation = donationRepository.findById(request.getDonationId())
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        // Verify user owns the donation
        if (!donation.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only add statements to your own donations");
        }
        
        Statement statement = Statement.builder()
                .donation(donation)
                .user(user)
                .message(request.getMessage())
                .build();
        
        statement = statementRepository.save(statement);
        return mapToDTO(statement);
    }
    
    public List<StatementDTO> getAllStatements() {
        return statementRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<StatementDTO> getDonationStatements(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        return statementRepository.findByDonation(donation).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private StatementDTO mapToDTO(Statement statement) {
        StatementDTO dto = modelMapper.map(statement, StatementDTO.class);
        dto.setDonationId(statement.getDonation().getId());
        dto.setUserId(statement.getUser().getId());
        dto.setUserName(statement.getUser().getName());
        dto.setDonationAmount(statement.getDonation().getAmount());
        return dto;
    }
}

