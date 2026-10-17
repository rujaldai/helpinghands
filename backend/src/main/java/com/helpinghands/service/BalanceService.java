package com.helpinghands.service;

import com.helpinghands.dto.BalanceDTO;
import com.helpinghands.entity.DonationLedger;
import com.helpinghands.entity.Institution;
import com.helpinghands.exception.ResourceNotFoundException;
import com.helpinghands.repository.DonationLedgerRepository;
import com.helpinghands.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceService {
    
    private final InstitutionRepository institutionRepository;

    private final DonationLedgerRepository donationLedgerRepository;

    @Transactional
    public BalanceDTO getBalance(Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Institution not found"));
        return calculateBalance(institution);
    }
    
    public BalanceDTO getHostCompanyBalance() {
        Institution hostCompany = institutionRepository.findByIsHostCompanyTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Host company"));
        
        return calculateBalance(hostCompany);
    }
    
    public List<BalanceDTO> getAllBalances() {
        return institutionRepository.findAll().stream()
                .map(this::calculateBalance)
                .collect(Collectors.toList());
    }
    
    private BalanceDTO calculateBalance(Institution institution) {
        BigDecimal incomingBalance = donationLedgerRepository.getTotalIncomingAmount(institution.getId());
        BigDecimal outgoingBalance = donationLedgerRepository.getTotalOutgoingAmount(institution.getId());

        
        BigDecimal availableBalance = incomingBalance
                .subtract(outgoingBalance);
        
        return BalanceDTO.builder()
                .institutionId(institution.getId())
                .institutionName(institution.getName())
                .availableBalance(availableBalance)
                .build();
    }
}

