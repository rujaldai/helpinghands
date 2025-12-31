package com.helpinghands.controller;

import com.helpinghands.dto.CauseDTO;
import com.helpinghands.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/causes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CauseController {
    
    private static final Logger logger = LoggerFactory.getLogger(CauseController.class);
    private final CauseService causeService;
    
    @GetMapping
    public ResponseEntity<List<CauseDTO>> getAllCauses() {
        logger.info("Fetching all causes");
        List<CauseDTO> causes = causeService.getAllCauses();
        logger.debug("Retrieved {} causes", causes.size());
        return ResponseEntity.ok(causes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CauseDTO> getCauseById(@PathVariable Long id) {
        logger.info("Fetching cause with ID: {}", id);
        CauseDTO cause = causeService.getCauseById(id);
        logger.debug("Retrieved cause: {}", cause);
        return ResponseEntity.ok(cause);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CauseDTO> createCause(@RequestBody CauseDTO causeDTO) {
        logger.info("Creating new cause with title: {}", causeDTO.getName());
        CauseDTO createdCause = causeService.createCause(causeDTO);
        logger.info("Successfully created cause with ID: {}", createdCause.getId());
        return ResponseEntity.ok(createdCause);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CauseDTO> updateCause(
            @PathVariable Long id,
            @RequestBody CauseDTO causeDTO) {
        logger.info("Updating cause with ID: {}", id);
        CauseDTO updatedCause = causeService.updateCause(id, causeDTO);
        logger.info("Successfully updated cause with ID: {}", id);
        return ResponseEntity.ok(updatedCause);
    }
}

