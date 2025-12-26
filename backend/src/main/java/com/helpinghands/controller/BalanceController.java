package com.helpinghands.controller;

import com.helpinghands.dto.BalanceDTO;
import com.helpinghands.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BalanceController {
    
    private final BalanceService balanceService;
    
    @GetMapping("/host-company")
    public ResponseEntity<BalanceDTO> getHostCompanyBalance() {
        return ResponseEntity.ok(balanceService.getHostCompanyBalance());
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long institutionId) {
        return ResponseEntity.ok(balanceService.getBalance(institutionId));
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<BalanceDTO>> getAllBalances() {
        return ResponseEntity.ok(balanceService.getAllBalances());
    }
}

