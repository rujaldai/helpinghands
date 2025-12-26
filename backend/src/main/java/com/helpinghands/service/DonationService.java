package com.helpinghands.service;

import com.helpinghands.dto.DonationDTO;
import com.helpinghands.dto.DonationRequest;
import com.helpinghands.entity.*;
import com.helpinghands.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {
    
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final CauseRepository causeRepository;
    private final ModelMapper modelMapper;
    
    @Transactional
    public DonationDTO createDonation(DonationRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Donation.DonationBuilder builder = Donation.builder()
                .user(user)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .toHostCompany(request.getToHostCompany() != null && request.getToHostCompany());
        
        // If donating to host company
        if (request.getToHostCompany() != null && request.getToHostCompany()) {
            Institution hostCompany = institutionRepository.findByIsHostCompanyTrue()
                    .orElseThrow(() -> new RuntimeException("Host company not found"));
            builder.institution(hostCompany);
        } else if (request.getInstitutionId() != null) {
            Institution institution = institutionRepository.findById(request.getInstitutionId())
                    .orElseThrow(() -> new RuntimeException("Institution not found"));
            builder.institution(institution);
        } else if (request.getCauseId() != null) {
            Cause cause = causeRepository.findById(request.getCauseId())
                    .orElseThrow(() -> new RuntimeException("Cause not found"));
            builder.cause(cause);
        } else {
            throw new RuntimeException("Either institution, cause, or toHostCompany must be specified");
        }
        
        Donation donation = donationRepository.save(builder.build());
        return mapToDTO(donation);
    }
    
    @Transactional
    public DonationDTO createGuestDonation(DonationRequest request) {
        User user;
        
        if (request.getGuestId() != null && !request.getGuestId().isEmpty()) {
            user = userRepository.findByGuestId(request.getGuestId())
                    .orElseThrow(() -> new RuntimeException("Guest ID not found"));
        } else {
            throw new RuntimeException("Guest ID is required for guest donations");
        }
        
        Donation.DonationBuilder builder = Donation.builder()
                .user(user)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .toHostCompany(request.getToHostCompany() != null && request.getToHostCompany());
        
        // If donating to host company
        if (request.getToHostCompany() != null && request.getToHostCompany()) {
            Institution hostCompany = institutionRepository.findByIsHostCompanyTrue()
                    .orElseThrow(() -> new RuntimeException("Host company not found"));
            builder.institution(hostCompany);
        } else if (request.getInstitutionId() != null) {
            Institution institution = institutionRepository.findById(request.getInstitutionId())
                    .orElseThrow(() -> new RuntimeException("Institution not found"));
            builder.institution(institution);
        } else if (request.getCauseId() != null) {
            Cause cause = causeRepository.findById(request.getCauseId())
                    .orElseThrow(() -> new RuntimeException("Cause not found"));
            builder.cause(cause);
        } else {
            throw new RuntimeException("Either institution, cause, or toHostCompany must be specified");
        }
        
        Donation donation = donationRepository.save(builder.build());
        return mapToDTO(donation);
    }
    
    public List<DonationDTO> getUserDonations(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return donationRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private DonationDTO mapToDTO(Donation donation) {
        DonationDTO dto = modelMapper.map(donation, DonationDTO.class);
        dto.setUserId(donation.getUser().getId());
        dto.setUserName(donation.getUser().getName());
        
        if (donation.getInstitution() != null) {
            dto.setInstitutionId(donation.getInstitution().getId());
            dto.setInstitutionName(donation.getInstitution().getName());
        }
        
        if (donation.getCause() != null) {
            dto.setCauseId(donation.getCause().getId());
            dto.setCauseName(donation.getCause().getName());
        }
        
        return dto;
    }
}

