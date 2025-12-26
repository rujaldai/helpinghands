package com.helpinghands.service;

import com.helpinghands.dto.BalanceDTO;
import com.helpinghands.entity.Institution;
import com.helpinghands.exception.ResourceNotFoundException;
import com.helpinghands.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceService {
    
    private final InstitutionRepository institutionRepository;
    
    public BalanceDTO getBalance(Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Institution", institutionId));
        
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
        BigDecimal totalDonations = institution.getDonations().stream()
                .map(donation -> donation.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalExpenses = institution.getExpenses().stream()
                .map(expense -> expense.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalOutgoing = institution.getOutgoingTransfers().stream()
                .map(transfer -> transfer.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalIncoming = institution.getIncomingTransfers().stream()
                .map(transfer -> transfer.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal availableBalance = totalDonations
                .add(totalIncoming)
                .subtract(totalExpenses)
                .subtract(totalOutgoing);
        
        return BalanceDTO.builder()
                .institutionId(institution.getId())
                .institutionName(institution.getName())
                .totalDonations(totalDonations)
                .totalExpenses(totalExpenses)
                .totalOutgoingTransfers(totalOutgoing)
                .totalIncomingTransfers(totalIncoming)
                .availableBalance(availableBalance)
                .build();
    }
}

