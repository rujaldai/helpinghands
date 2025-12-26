package com.helpinghands.controller;

import com.helpinghands.dto.InstitutionDTO;
import com.helpinghands.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InstitutionController {
    
    private final InstitutionService institutionService;
    
    @GetMapping
    public ResponseEntity<List<InstitutionDTO>> getAllInstitutions() {
        return ResponseEntity.ok(institutionService.getAllInstitutions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getInstitutionById(@PathVariable Long id) {
        return ResponseEntity.ok(institutionService.getInstitutionById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstitutionDTO> createInstitution(@RequestBody InstitutionDTO institutionDTO) {
        return ResponseEntity.ok(institutionService.createInstitution(institutionDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstitutionDTO> updateInstitution(
            @PathVariable Long id,
            @RequestBody InstitutionDTO institutionDTO) {
        return ResponseEntity.ok(institutionService.updateInstitution(id, institutionDTO));
    }
}

