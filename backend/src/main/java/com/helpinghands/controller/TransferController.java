package com.helpinghands.controller;

import com.helpinghands.dto.TransferDTO;
import com.helpinghands.dto.TransferRequest;
import com.helpinghands.exception.ResourceNotFoundException;
import com.helpinghands.repository.InstitutionRepository;
import com.helpinghands.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransferController {
    
    private final TransferService transferService;
    private final InstitutionRepository institutionRepository;
    
    @PostMapping("/from-host")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransferDTO> createTransferFromHost(@Valid @RequestBody TransferRequest request) {
        Long hostCompanyId = institutionRepository.findByIsHostCompanyTrue()
                .map(institution -> institution.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Host company"));
        
        return ResponseEntity.ok(transferService.createTransfer(request, hostCompanyId));
    }
    
    @PostMapping("/from-institution/{institutionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransferDTO> createTransferFromInstitution(
            @PathVariable Long institutionId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.createTransfer(request, institutionId));
    }
    
    @GetMapping
    public ResponseEntity<List<TransferDTO>> getAllTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<TransferDTO>> getTransfersByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(transferService.getTransfersByInstitution(institutionId));
    }
}

