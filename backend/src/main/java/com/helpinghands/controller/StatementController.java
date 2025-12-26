package com.helpinghands.controller;

import com.helpinghands.dto.StatementDTO;
import com.helpinghands.dto.StatementRequest;
import com.helpinghands.service.StatementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatementController {
    
    private final StatementService statementService;
    
    @PostMapping
    public ResponseEntity<StatementDTO> createStatement(
            @Valid @RequestBody StatementRequest request,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(statementService.createStatement(
                request, authentication.getName()));
    }
    
    @GetMapping
    public ResponseEntity<List<StatementDTO>> getAllStatements() {
        return ResponseEntity.ok(statementService.getAllStatements());
    }
    
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<List<StatementDTO>> getDonationStatements(@PathVariable Long donationId) {
        return ResponseEntity.ok(statementService.getDonationStatements(donationId));
    }
}

