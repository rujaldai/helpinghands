package com.helpinghands.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private BigDecimal totalDonations;
    private Long totalDonators;
    private Long totalInstitutions;
    private Long totalCauses;
    private List<TopDonatorDTO> topDonators;
    private List<TopInstitutionDTO> topInstitutions;
    private List<TopCauseDTO> topCauses;
    private List<StatementDTO> recentStatements;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopDonatorDTO {
        private Long userId;
        private String userName;
        private BigDecimal totalAmount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopInstitutionDTO {
        private Long institutionId;
        private String institutionName;
        private BigDecimal totalAmount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopCauseDTO {
        private Long causeId;
        private String causeName;
        private BigDecimal totalAmount;
    }
}

