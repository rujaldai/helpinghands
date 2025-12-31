package com.helpinghands.controller;

import com.helpinghands.dto.StatementDTO;
import com.helpinghands.dto.StatementRequest;
import com.helpinghands.service.StatementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatementController {
    
    private static final Logger logger = LoggerFactory.getLogger(StatementController.class);
    private final StatementService statementService;
    
    @PostMapping
    public ResponseEntity<StatementDTO> createStatement(
            @Valid @RequestBody StatementRequest request,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized attempt to create statement");
            return ResponseEntity.badRequest().build();
        }
        logger.info("Creating statement for user: {}", authentication.getName());
        StatementDTO statement = statementService.createStatement(request, authentication.getName());
        logger.info("Successfully created statement with ID: {} for donation ID: {}", 
            statement.getId(), request.getDonationId());
        return ResponseEntity.ok(statement);
    }
    
    @GetMapping
    public ResponseEntity<List<StatementDTO>> getAllStatements() {
        logger.info("Fetching all statements");
        List<StatementDTO> statements = statementService.getAllStatements();
        logger.debug("Retrieved {} statements", statements.size());
        return ResponseEntity.ok(statements);
    }
    
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<List<StatementDTO>> getDonationStatements(@PathVariable Long donationId) {
        logger.info("Fetching statements for donation ID: {}", donationId);
        List<StatementDTO> statements = statementService.getDonationStatements(donationId);
        logger.debug("Retrieved {} statements for donation ID: {}", statements.size(), donationId);
        return ResponseEntity.ok(statements);
    }
}

