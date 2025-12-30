package com.helpinghands.controller;

import com.helpinghands.dto.DashboardStatsDTO;
import com.helpinghands.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final DashboardService dashboardService;
    
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        logger.info("Fetching dashboard statistics");
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        logger.debug("Retrieved dashboard statistics: {}", stats);
        return ResponseEntity.ok(stats);
    }
}

