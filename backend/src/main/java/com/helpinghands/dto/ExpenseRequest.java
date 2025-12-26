package com.helpinghands.dto;

import com.helpinghands.entity.ExpenseCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private String currency = "USD";
    
    @NotNull
    private ExpenseCategory category;
    
    private String description;
    
    private String recipient; // For DONATION_TO_PERSON
    
    private Long recipientInstitutionId; // For DONATION_TO_INSTITUTION
}

