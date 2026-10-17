package com.helpinghands.dto;

import com.helpinghands.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DonationRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private Currency currency = Currency.USD;
    
    private Long institutionId;
    
    private Long causeId;
    
    private Boolean toHostCompany = false; // Donate directly to host company
    
    private String guestId; // For guest donations
}

