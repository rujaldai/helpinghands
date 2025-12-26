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
public class DonationDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long institutionId;
    private String institutionName;
    private Long causeId;
    private String causeName;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;
}

