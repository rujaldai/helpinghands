package com.helpinghands.controller;

import com.helpinghands.dto.MoneyFlowDTO;
import com.helpinghands.service.MoneyFlowService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/money-flows")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MoneyFlowController {
    
    private static final Logger logger = LoggerFactory.getLogger(MoneyFlowController.class);
    private final MoneyFlowService moneyFlowService;
    
    @GetMapping
    public ResponseEntity<List<MoneyFlowDTO>> getAllMoneyFlows() {
        logger.info("Fetching all money flows");
        List<MoneyFlowDTO> moneyFlows = moneyFlowService.getAllMoneyFlows();
        logger.debug("Retrieved {} money flows", moneyFlows.size());
        return ResponseEntity.ok(moneyFlows);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MoneyFlowDTO>> getMoneyFlowsByUser(@PathVariable Long userId) {
        logger.info("Fetching money flows for user ID: {}", userId);
        List<MoneyFlowDTO> moneyFlows = moneyFlowService.getMoneyFlowsByUserId(userId);
        logger.debug("Retrieved {} money flows for user ID: {}", moneyFlows.size(), userId);
        return ResponseEntity.ok(moneyFlows);
    }
    
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<List<MoneyFlowDTO>> getMoneyFlowsByDonation(@PathVariable Long donationId) {
        logger.info("Fetching money flows for donation ID: {}", donationId);
        List<MoneyFlowDTO> moneyFlows = moneyFlowService.getMoneyFlowsByDonationId(donationId);
        logger.debug("Retrieved {} money flows for donation ID: {}", moneyFlows.size(), donationId);
        return ResponseEntity.ok(moneyFlows);
    }
}

