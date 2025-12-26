package com.helpinghands.controller;

import com.helpinghands.dto.DonationDTO;
import com.helpinghands.dto.DonationRequest;
import com.helpinghands.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonationController {
    
    private final DonationService donationService;
    
    @PostMapping
    public ResponseEntity<DonationDTO> createDonation(
            @Valid @RequestBody DonationRequest request,
            Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(donationService.createDonation(
                    request, authentication.getName()));
        } else {
            return ResponseEntity.ok(donationService.createGuestDonation(request));
        }
    }
    
    @GetMapping("/my")
    public ResponseEntity<List<DonationDTO>> getMyDonations(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(donationService.getUserDonations(authentication.getName()));
    }
    
    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        return ResponseEntity.ok(donationService.getAllDonations());
    }
}

