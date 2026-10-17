package com.helpinghands.dto;

import com.helpinghands.enums.Currency;
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
public class TransferDTO {
    private Long id;
    private Long fromInstitutionId;
    private String fromInstitutionName;
    private Long toInstitutionId;
    private String toInstitutionName;
    private BigDecimal amount;
    private Currency currency;
    private String description;
    private LocalDateTime createdAt;
}

