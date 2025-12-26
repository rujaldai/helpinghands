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
public class InstitutionDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private Boolean isHostCompany;
    private BigDecimal totalDonations;
    private LocalDateTime createdAt;
}

