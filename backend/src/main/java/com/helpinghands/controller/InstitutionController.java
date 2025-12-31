package com.helpinghands.controller;

import com.helpinghands.dto.InstitutionDTO;
import com.helpinghands.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InstitutionController {
    
    private static final Logger logger = LoggerFactory.getLogger(InstitutionController.class);
    private final InstitutionService institutionService;
    
    @GetMapping
    public ResponseEntity<List<InstitutionDTO>> getAllInstitutions() {
        logger.info("Fetching all institutions");
        List<InstitutionDTO> institutions = institutionService.getAllInstitutions();
        logger.debug("Retrieved {} institutions", institutions.size());
        return ResponseEntity.ok(institutions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getInstitutionById(@PathVariable Long id) {
        logger.info("Fetching institution with ID: {}", id);
        InstitutionDTO institution = institutionService.getInstitutionById(id);
        logger.debug("Retrieved institution: {}", institution);
        return ResponseEntity.ok(institution);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstitutionDTO> createInstitution(@RequestBody InstitutionDTO institutionDTO) {
        logger.info("Creating new institution: {}", institutionDTO.getName());
        InstitutionDTO createdInstitution = institutionService.createInstitution(institutionDTO);
        logger.info("Successfully created institution with ID: {}", createdInstitution.getId());
        return ResponseEntity.ok(createdInstitution);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstitutionDTO> updateInstitution(
            @PathVariable Long id,
            @RequestBody InstitutionDTO institutionDTO) {
        logger.info("Updating institution with ID: {}", id);
        InstitutionDTO updatedInstitution = institutionService.updateInstitution(id, institutionDTO);
        logger.info("Successfully updated institution with ID: {}", id);
        return ResponseEntity.ok(updatedInstitution);
    }
}

