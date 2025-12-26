package com.helpinghands.controller;

import com.helpinghands.dto.CauseDTO;
import com.helpinghands.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/causes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CauseController {
    
    private final CauseService causeService;
    
    @GetMapping
    public ResponseEntity<List<CauseDTO>> getAllCauses() {
        return ResponseEntity.ok(causeService.getAllCauses());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CauseDTO> getCauseById(@PathVariable Long id) {
        return ResponseEntity.ok(causeService.getCauseById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CauseDTO> createCause(@RequestBody CauseDTO causeDTO) {
        return ResponseEntity.ok(causeService.createCause(causeDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CauseDTO> updateCause(
            @PathVariable Long id,
            @RequestBody CauseDTO causeDTO) {
        return ResponseEntity.ok(causeService.updateCause(id, causeDTO));
    }
}

