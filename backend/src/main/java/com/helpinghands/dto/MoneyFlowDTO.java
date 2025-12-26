package com.helpinghands.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyFlowDTO {
    private Long id;
    private Long donationId;
    private Long userId;
    private String userName;
    private BigDecimal donationAmount;
    private BigDecimal flowAmount;
    private String currency;
    private Long expenseId;
    private String expenseDescription;
    private Long transferId;
    private String transferDescription;
    private LocalDateTime createdAt;
}

