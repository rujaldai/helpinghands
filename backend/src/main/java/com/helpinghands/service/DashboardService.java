package com.helpinghands.service;

import com.helpinghands.dto.DashboardStatsDTO;
import com.helpinghands.dto.StatementDTO;
import com.helpinghands.entity.Cause;
import com.helpinghands.entity.Donation;
import com.helpinghands.entity.Institution;
import com.helpinghands.entity.User;
import com.helpinghands.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final CauseRepository causeRepository;
    private final StatementRepository statementRepository;
    
    public DashboardStatsDTO getDashboardStats() {
        // Calculate total donations
        BigDecimal totalDonations = donationRepository.findAll().stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Get counts
        Long totalDonators = userRepository.count();
        Long totalInstitutions = institutionRepository.count();
        Long totalCauses = causeRepository.count();
        
        // Get top donators (limit 10)
        List<DashboardStatsDTO.TopDonatorDTO> topDonators = donationRepository.findTopDonators().stream()
                .limit(10)
                .map(result -> {
                    User user = (User) result[0];
                    BigDecimal total = (BigDecimal) result[1];
                    return DashboardStatsDTO.TopDonatorDTO.builder()
                            .userId(user.getId())
                            .userName(user.getName())
                            .totalAmount(total)
                            .build();
                })
                .collect(Collectors.toList());
        
        // Get top institutions (limit 10)
        List<DashboardStatsDTO.TopInstitutionDTO> topInstitutions = donationRepository.findTopInstitutions().stream()
                .limit(10)
                .map(result -> {
                    Institution institution = (Institution) result[0];
                    BigDecimal total = (BigDecimal) result[1];
                    return DashboardStatsDTO.TopInstitutionDTO.builder()
                            .institutionId(institution.getId())
                            .institutionName(institution.getName())
                            .totalAmount(total)
                            .build();
                })
                .collect(Collectors.toList());
        
        // Get top causes (limit 10)
        List<DashboardStatsDTO.TopCauseDTO> topCauses = donationRepository.findTopCauses().stream()
                .limit(10)
                .map(result -> {
                    Cause cause = (Cause) result[0];
                    BigDecimal total = (BigDecimal) result[1];
                    return DashboardStatsDTO.TopCauseDTO.builder()
                            .causeId(cause.getId())
                            .causeName(cause.getName())
                            .totalAmount(total)
                            .build();
                })
                .collect(Collectors.toList());
        
        // Get recent statements (limit 20)
        List<StatementDTO> recentStatements = statementRepository.findAllByOrderByCreatedAtDesc().stream()
                .limit(20)
                .map(statement -> {
                    StatementDTO dto = new StatementDTO();
                    dto.setId(statement.getId());
                    dto.setDonationId(statement.getDonation().getId());
                    dto.setUserId(statement.getUser().getId());
                    dto.setUserName(statement.getUser().getName());
                    dto.setMessage(statement.getMessage());
                    dto.setDonationAmount(statement.getDonation().getAmount());
                    dto.setCreatedAt(statement.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
        
        return DashboardStatsDTO.builder()
                .totalDonations(totalDonations)
                .totalDonators(totalDonators)
                .totalInstitutions(totalInstitutions)
                .totalCauses(totalCauses)
                .topDonators(topDonators)
                .topInstitutions(topInstitutions)
                .topCauses(topCauses)
                .recentStatements(recentStatements)
                .build();
    }
}

