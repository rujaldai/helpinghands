package com.helpinghands.controller;

import com.helpinghands.dto.BalanceDTO;
import com.helpinghands.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BalanceController {
    
    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    private final BalanceService balanceService;
    
    @GetMapping("/host-company")
    public ResponseEntity<BalanceDTO> getHostCompanyBalance() {
        logger.info("Fetching host company balance");
        BalanceDTO balance = balanceService.getHostCompanyBalance();
        logger.debug("Host company balance: {}", balance);
        return ResponseEntity.ok(balance);
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long institutionId) {
        logger.info("Fetching balance for institution ID: {}", institutionId);
        BalanceDTO balance = balanceService.getBalance(institutionId);
        logger.debug("Balance for institution {}: {}", institutionId, balance);
        return ResponseEntity.ok(balance);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<BalanceDTO>> getAllBalances() {
        logger.info("Fetching all institution balances");
        List<BalanceDTO> balances = balanceService.getAllBalances();
        logger.debug("Retrieved {} institution balances", balances.size());
        return ResponseEntity.ok(balances);
    }
}

