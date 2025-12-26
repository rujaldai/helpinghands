package com.helpinghands.dto;

import com.helpinghands.entity.ExpenseCategory;
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
public class ExpenseDTO {
    private Long id;
    private Long institutionId;
    private String institutionName;
    private ExpenseCategory category;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String recipient;
    private Long recipientInstitutionId;
    private String recipientInstitutionName;
    private LocalDateTime createdAt;
}

