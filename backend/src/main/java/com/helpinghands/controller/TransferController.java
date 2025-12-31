package com.helpinghands.controller;

import com.helpinghands.dto.TransferDTO;
import com.helpinghands.dto.TransferRequest;
import com.helpinghands.exception.ResourceNotFoundException;
import com.helpinghands.repository.InstitutionRepository;
import com.helpinghands.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransferController {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);
    private final TransferService transferService;
    private final InstitutionRepository institutionRepository;
    
    @PostMapping("/from-host")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransferDTO> createTransferFromHost(@Valid @RequestBody TransferRequest request) {
        logger.info("Creating transfer from host company");
        Long hostCompanyId = institutionRepository.findByIsHostCompanyTrue()
                .map(institution -> institution.getId())
                .orElseThrow(() -> {
                    logger.error("Host company not found");
                    return new ResourceNotFoundException("Host company");
                });
        
        logger.debug("Creating transfer with request: {}", request);
        TransferDTO transfer = transferService.createTransfer(request, hostCompanyId);
        logger.info("Successfully created transfer with ID: {} from host company to institution ID: {}", 
            transfer.getId(), request.getToInstitutionId());
        return ResponseEntity.ok(transfer);
    }
    
    @PostMapping("/from-institution/{institutionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransferDTO> createTransferFromInstitution(
            @PathVariable Long institutionId,
            @Valid @RequestBody TransferRequest request) {
        logger.info("Creating transfer from institution ID: {}", institutionId);
        logger.debug("Transfer request: {}", request);
        TransferDTO transfer = transferService.createTransfer(request, institutionId);
        logger.info("Successfully created transfer with ID: {} from institution ID: {} to {}", 
            transfer.getId(), institutionId, 
            transfer.getToInstitutionId() != null ? "institution ID: " + transfer.getToInstitutionId() : "host company");
        return ResponseEntity.ok(transfer);
    }
    
    @GetMapping
    public ResponseEntity<List<TransferDTO>> getAllTransfers() {
        logger.info("Fetching all transfers");
        List<TransferDTO> transfers = transferService.getAllTransfers();
        logger.debug("Retrieved {} transfers", transfers.size());
        return ResponseEntity.ok(transfers);
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<TransferDTO>> getTransfersByInstitution(@PathVariable Long institutionId) {
        logger.info("Fetching transfers for institution ID: {}", institutionId);
        List<TransferDTO> transfers = transferService.getTransfersByInstitution(institutionId);
        logger.debug("Retrieved {} transfers for institution ID: {}", transfers.size(), institutionId);
        return ResponseEntity.ok(transfers);
    }
}

