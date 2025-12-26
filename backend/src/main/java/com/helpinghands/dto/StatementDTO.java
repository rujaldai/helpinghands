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
public class StatementDTO {
    private Long id;
    private Long donationId;
    private Long userId;
    private String userName;
    private String message;
    private BigDecimal donationAmount;
    private LocalDateTime createdAt;
}

