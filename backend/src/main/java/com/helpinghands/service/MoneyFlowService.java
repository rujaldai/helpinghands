package com.helpinghands.service;

import com.helpinghands.dto.MoneyFlowDTO;
import com.helpinghands.entity.DonationLedger;
import com.helpinghands.repository.DonationLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoneyFlowService {
    
    private final DonationLedgerRepository donationLedgerRepository;
    private final ModelMapper modelMapper;
    
    public List<MoneyFlowDTO> getMoneyFlowsByUserId(Long userId) {
        return donationLedgerRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MoneyFlowDTO> getMoneyFlowsByDonationId(Long donationId) {
        return donationLedgerRepository.findAll().stream()
                .filter(mf -> mf.getDonation().getId().equals(donationId))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MoneyFlowDTO> getAllMoneyFlows() {
        return donationLedgerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private MoneyFlowDTO mapToDTO(DonationLedger donationLedger) {
        MoneyFlowDTO dto = modelMapper.map(donationLedger, MoneyFlowDTO.class);
        dto.setDonationId(donationLedger.getDonation().getId());
        dto.setUserId(donationLedger.getDonation().getUser().getId());
        dto.setUserName(donationLedger.getDonation().getUser().getName());
        dto.setDonationAmount(donationLedger.getDonation().getAmount());
        
        if (donationLedger.getExpense() != null) {
            dto.setExpenseId(donationLedger.getExpense().getId());
            dto.setExpenseDescription(donationLedger.getExpense().getDescription());
        }
        
        if (donationLedger.getTransfer() != null) {
            dto.setTransferId(donationLedger.getTransfer().getId());
            dto.setTransferDescription(donationLedger.getTransfer().getDescription());
        }
        
        return dto;
    }
}

