package com.helpinghands.service;

import com.helpinghands.dto.BalanceDTO;
import com.helpinghands.dto.TransferDTO;
import com.helpinghands.dto.TransferRequest;
import com.helpinghands.entity.*;
import com.helpinghands.exception.InsufficientBalanceException;
import com.helpinghands.exception.ResourceNotFoundException;
import com.helpinghands.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferService {
    
    private final TransferRepository transferRepository;
    private final InstitutionRepository institutionRepository;
    private final DonationRepository donationRepository;
    private final MoneyFlowRepository moneyFlowRepository;
    private final BalanceService balanceService;
    private final ModelMapper modelMapper;
    
    @Transactional
    public TransferDTO createTransfer(TransferRequest request, Long fromInstitutionId) {
        Institution fromInstitution = institutionRepository.findById(fromInstitutionId)
                .orElseThrow(() -> new ResourceNotFoundException("From institution", fromInstitutionId));
        
        Institution toInstitution = institutionRepository.findById(request.getToInstitutionId())
                .orElseThrow(() -> new ResourceNotFoundException("To institution", request.getToInstitutionId()));
        
        // Check available balance
        BalanceDTO balance = balanceService.getBalance(fromInstitutionId);
        if (request.getAmount().compareTo(balance.getAvailableBalance()) > 0) {
            throw new InsufficientBalanceException(balance.getAvailableBalance());
        }
        
        Transfer transfer = Transfer.builder()
                .fromInstitution(fromInstitution)
                .toInstitution(toInstitution)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .build();
        
        transfer = transferRepository.save(transfer);
        
        // Create money flows - track which donations were used for this transfer
        createMoneyFlowsForTransfer(transfer, request.getAmount(), fromInstitution);
        
        return mapToDTO(transfer);
    }
    
    private void createMoneyFlowsForTransfer(Transfer transfer, BigDecimal transferAmount, Institution fromInstitution) {
        // If transferring from host company, track which user donations were used
        if (fromInstitution.getIsHostCompany() != null && fromInstitution.getIsHostCompany()) {
            List<Donation> hostCompanyDonations = donationRepository.findAll().stream()
                    .filter(d -> d.getToHostCompany() != null && d.getToHostCompany())
                    .collect(Collectors.toList());
            
            BigDecimal remainingAmount = transferAmount;
            
            for (Donation donation : hostCompanyDonations) {
                if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
                
                // Calculate how much of this donation is already used (expenses + transfers)
                BigDecimal usedAmount = donation.getMoneyFlows().stream()
                        .map(MoneyFlow::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                BigDecimal availableFromDonation = donation.getAmount().subtract(usedAmount);
                
                if (availableFromDonation.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal amountToUse = availableFromDonation.min(remainingAmount);
                    
                    MoneyFlow moneyFlow = MoneyFlow.builder()
                            .donation(donation)
                            .transfer(transfer)
                            .amount(amountToUse)
                            .currency(donation.getCurrency())
                            .build();
                    
                    moneyFlowRepository.save(moneyFlow);
                    remainingAmount = remainingAmount.subtract(amountToUse);
                }
            }
            
            if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
                throw new InsufficientBalanceException(remainingAmount);
            }
        }
    }
    
    public List<TransferDTO> getTransfersByInstitution(Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Institution", institutionId));
        
        List<Transfer> transfers = transferRepository.findByFromInstitution(institution);
        transfers.addAll(transferRepository.findByToInstitution(institution));
        
        return transfers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<TransferDTO> getAllTransfers() {
        return transferRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private TransferDTO mapToDTO(Transfer transfer) {
        TransferDTO dto = modelMapper.map(transfer, TransferDTO.class);
        dto.setFromInstitutionId(transfer.getFromInstitution().getId());
        dto.setFromInstitutionName(transfer.getFromInstitution().getName());
        dto.setToInstitutionId(transfer.getToInstitution().getId());
        dto.setToInstitutionName(transfer.getToInstitution().getName());
        return dto;
    }
}

