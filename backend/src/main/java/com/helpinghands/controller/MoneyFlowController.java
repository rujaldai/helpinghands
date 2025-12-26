package com.helpinghands.controller;

import com.helpinghands.dto.MoneyFlowDTO;
import com.helpinghands.service.MoneyFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/money-flows")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MoneyFlowController {
    
    private final MoneyFlowService moneyFlowService;
    
    @GetMapping
    public ResponseEntity<List<MoneyFlowDTO>> getAllMoneyFlows() {
        return ResponseEntity.ok(moneyFlowService.getAllMoneyFlows());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MoneyFlowDTO>> getMoneyFlowsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(moneyFlowService.getMoneyFlowsByUserId(userId));
    }
    
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<List<MoneyFlowDTO>> getMoneyFlowsByDonation(@PathVariable Long donationId) {
        return ResponseEntity.ok(moneyFlowService.getMoneyFlowsByDonationId(donationId));
    }
}

