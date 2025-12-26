package com.helpinghands.service;

import com.helpinghands.dto.InstitutionDTO;
import com.helpinghands.entity.Donation;
import com.helpinghands.entity.Institution;
import com.helpinghands.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    
    private final InstitutionRepository institutionRepository;
    private final ModelMapper modelMapper;
    
    public List<InstitutionDTO> getAllInstitutions() {
        return institutionRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public InstitutionDTO getInstitutionById(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        return mapToDTO(institution);
    }
    
    @Transactional
    public InstitutionDTO createInstitution(InstitutionDTO institutionDTO) {
        Institution institution = Institution.builder()
                .name(institutionDTO.getName())
                .description(institutionDTO.getDescription())
                .active(true)
                .build();
        
        institution = institutionRepository.save(institution);
        return mapToDTO(institution);
    }
    
    @Transactional
    public InstitutionDTO updateInstitution(Long id, InstitutionDTO institutionDTO) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        
        if (institutionDTO.getName() != null) {
            institution.setName(institutionDTO.getName());
        }
        if (institutionDTO.getDescription() != null) {
            institution.setDescription(institutionDTO.getDescription());
        }
        if (institutionDTO.getActive() != null) {
            institution.setActive(institutionDTO.getActive());
        }
        
        institution = institutionRepository.save(institution);
        return mapToDTO(institution);
    }
    
    private InstitutionDTO mapToDTO(Institution institution) {
        InstitutionDTO dto = modelMapper.map(institution, InstitutionDTO.class);
        BigDecimal total = institution.getDonations().stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalDonations(total);
        return dto;
    }
}

