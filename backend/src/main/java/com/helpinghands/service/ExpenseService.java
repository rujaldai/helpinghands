package com.helpinghands.service;

import com.helpinghands.dto.ExpenseDTO;
import com.helpinghands.dto.ExpenseRequest;
import com.helpinghands.entity.*;
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
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final InstitutionRepository institutionRepository;
    private final DonationRepository donationRepository;
    private final MoneyFlowRepository moneyFlowRepository;
    private final ModelMapper modelMapper;
    
    @Transactional
    public ExpenseDTO createExpense(ExpenseRequest request) {
        // Get host company
        Institution hostCompany = institutionRepository.findByIsHostCompanyTrue()
                .orElseThrow(() -> new RuntimeException("Host company not found"));
        
        // Check available balance
        BigDecimal availableBalance = calculateAvailableBalance(hostCompany.getId());
        if (request.getAmount().compareTo(availableBalance) > 0) {
            throw new RuntimeException("Insufficient balance. Available: " + availableBalance);
        }
        
        Expense expense = Expense.builder()
                .institution(hostCompany)
                .category(request.getCategory())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .recipient(request.getRecipient())
                .build();
        
        // Set recipient institution if applicable
        if (request.getRecipientInstitutionId() != null) {
            Institution recipientInstitution = institutionRepository.findById(request.getRecipientInstitutionId())
                    .orElseThrow(() -> new RuntimeException("Recipient institution not found"));
            expense.setRecipientInstitution(recipientInstitution);
        }
        
        expense = expenseRepository.save(expense);
        
        // Create money flows - track which donations were used for this expense
        createMoneyFlowsForExpense(expense, request.getAmount());
        
        return mapToDTO(expense);
    }
    
    private void createMoneyFlowsForExpense(Expense expense, BigDecimal expenseAmount) {
        // Get all donations to host company that haven't been fully allocated
        List<Donation> hostCompanyDonations = donationRepository.findAll().stream()
                .filter(d -> d.getToHostCompany() != null && d.getToHostCompany())
                .collect(Collectors.toList());
        
        BigDecimal remainingAmount = expenseAmount;
        
        for (Donation donation : hostCompanyDonations) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            // Calculate how much of this donation is already used
            BigDecimal usedAmount = donation.getMoneyFlows().stream()
                    .filter(mf -> mf.getExpense() != null)
                    .map(MoneyFlow::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal availableFromDonation = donation.getAmount().subtract(usedAmount);
            
            if (availableFromDonation.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal amountToUse = availableFromDonation.min(remainingAmount);
                
                MoneyFlow moneyFlow = MoneyFlow.builder()
                        .donation(donation)
                        .expense(expense)
                        .amount(amountToUse)
                        .currency(donation.getCurrency())
                        .build();
                
                moneyFlowRepository.save(moneyFlow);
                remainingAmount = remainingAmount.subtract(amountToUse);
            }
        }
        
        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Insufficient unallocated donations to cover expense");
        }
    }
    
    public List<ExpenseDTO> getExpensesByInstitution(Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        
        return expenseRepository.findByInstitutionOrderByCreatedAtDesc(institution).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO dto = modelMapper.map(expense, ExpenseDTO.class);
        dto.setInstitutionId(expense.getInstitution().getId());
        dto.setInstitutionName(expense.getInstitution().getName());
        
        if (expense.getRecipientInstitution() != null) {
            dto.setRecipientInstitutionId(expense.getRecipientInstitution().getId());
            dto.setRecipientInstitutionName(expense.getRecipientInstitution().getName());
        }
        
        return dto;
    }
    
    private BigDecimal calculateAvailableBalance(Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        
        // Reload to get fresh data
        institution = institutionRepository.findById(institutionId).orElse(institution);
        
        BigDecimal totalDonations = institution.getDonations().stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalExpenses = institution.getExpenses().stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalOutgoing = institution.getOutgoingTransfers().stream()
                .map(Transfer::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalIncoming = institution.getIncomingTransfers().stream()
                .map(Transfer::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalDonations.add(totalIncoming).subtract(totalExpenses).subtract(totalOutgoing);
    }
}

