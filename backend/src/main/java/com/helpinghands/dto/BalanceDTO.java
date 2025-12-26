package com.helpinghands.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    private Long institutionId;
    private String institutionName;
    private BigDecimal totalDonations;
    private BigDecimal totalExpenses;
    private BigDecimal totalOutgoingTransfers;
    private BigDecimal totalIncomingTransfers;
    private BigDecimal availableBalance; // donations - expenses - outgoing + incoming
}

