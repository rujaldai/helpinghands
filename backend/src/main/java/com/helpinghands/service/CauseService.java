package com.helpinghands.service;

import com.helpinghands.dto.CauseDTO;
import com.helpinghands.entity.Cause;
import com.helpinghands.repository.CauseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CauseService {
    
    private final CauseRepository causeRepository;
    private final ModelMapper modelMapper;
    
    public List<CauseDTO> getAllCauses() {
        return causeRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public CauseDTO getCauseById(Long id) {
        Cause cause = causeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cause not found"));
        return mapToDTO(cause);
    }
    
    @Transactional
    public CauseDTO createCause(CauseDTO causeDTO) {
        if (causeRepository.findByName(causeDTO.getName()).isPresent()) {
            throw new RuntimeException("Cause with this name already exists");
        }
        
        Cause cause = Cause.builder()
                .name(causeDTO.getName())
                .description(causeDTO.getDescription())
                .active(true)
                .build();
        
        cause = causeRepository.save(cause);
        return mapToDTO(cause);
    }
    
    @Transactional
    public CauseDTO updateCause(Long id, CauseDTO causeDTO) {
        Cause cause = causeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cause not found"));
        
        if (causeDTO.getName() != null) {
            cause.setName(causeDTO.getName());
        }
        if (causeDTO.getDescription() != null) {
            cause.setDescription(causeDTO.getDescription());
        }
        if (causeDTO.getActive() != null) {
            cause.setActive(causeDTO.getActive());
        }
        
        cause = causeRepository.save(cause);
        return mapToDTO(cause);
    }
    
    private CauseDTO mapToDTO(Cause cause) {
        CauseDTO dto = modelMapper.map(cause, CauseDTO.class);
        BigDecimal total = cause.getDonations().stream()
                .map(donation -> donation.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalDonations(total);
        return dto;
    }
}

