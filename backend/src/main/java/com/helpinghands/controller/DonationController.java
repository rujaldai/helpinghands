package com.helpinghands.controller;

import com.helpinghands.dto.DonationDTO;
import com.helpinghands.dto.DonationRequest;
import com.helpinghands.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonationController {
    
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);
    private final DonationService donationService;
    
    @PostMapping
    public ResponseEntity<DonationDTO> createDonation(
            @Valid @RequestBody DonationRequest request,
            Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("Creating donation for authenticated user: {}", authentication.getName());
            DonationDTO donation = donationService.createDonation(request, authentication.getName());
            logger.info("Successfully created donation with ID: {}", donation.getId());
            return ResponseEntity.ok(donation);
        } else {
            logger.info("Creating guest donation");
            DonationDTO guestDonation = donationService.createGuestDonation(request);
            logger.info("Successfully created guest donation with ID: {}", guestDonation.getId());
            return ResponseEntity.ok(guestDonation);
        }
    }
    
    @GetMapping("/my")
    public ResponseEntity<List<DonationDTO>> getMyDonations(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized attempt to access user donations");
            return ResponseEntity.badRequest().build();
        }
        logger.info("Fetching donations for user: {}", authentication.getName());
        List<DonationDTO> donations = donationService.getUserDonations(authentication.getName());
        logger.debug("Retrieved {} donations for user: {}", donations.size(), authentication.getName());
        return ResponseEntity.ok(donations);
    }
    
    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        logger.info("Fetching all donations");
        List<DonationDTO> donations = donationService.getAllDonations();
        logger.debug("Retrieved {} total donations", donations.size());
        return ResponseEntity.ok(donations);
    }
}

