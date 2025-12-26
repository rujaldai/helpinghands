package com.helpinghands.service;

import com.helpinghands.dto.MoneyFlowDTO;
import com.helpinghands.entity.MoneyFlow;
import com.helpinghands.repository.MoneyFlowRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoneyFlowService {
    
    private final MoneyFlowRepository moneyFlowRepository;
    private final ModelMapper modelMapper;
    
    public List<MoneyFlowDTO> getMoneyFlowsByUserId(Long userId) {
        return moneyFlowRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MoneyFlowDTO> getMoneyFlowsByDonationId(Long donationId) {
        return moneyFlowRepository.findAll().stream()
                .filter(mf -> mf.getDonation().getId().equals(donationId))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MoneyFlowDTO> getAllMoneyFlows() {
        return moneyFlowRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private MoneyFlowDTO mapToDTO(MoneyFlow moneyFlow) {
        MoneyFlowDTO dto = modelMapper.map(moneyFlow, MoneyFlowDTO.class);
        dto.setDonationId(moneyFlow.getDonation().getId());
        dto.setUserId(moneyFlow.getDonation().getUser().getId());
        dto.setUserName(moneyFlow.getDonation().getUser().getName());
        dto.setDonationAmount(moneyFlow.getDonation().getAmount());
        
        if (moneyFlow.getExpense() != null) {
            dto.setExpenseId(moneyFlow.getExpense().getId());
            dto.setExpenseDescription(moneyFlow.getExpense().getDescription());
        }
        
        if (moneyFlow.getTransfer() != null) {
            dto.setTransferId(moneyFlow.getTransfer().getId());
            dto.setTransferDescription(moneyFlow.getTransfer().getDescription());
        }
        
        return dto;
    }
}

