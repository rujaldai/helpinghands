package com.helpinghands.controller;

import com.helpinghands.dto.ExpenseDTO;
import com.helpinghands.dto.ExpenseRequest;
import com.helpinghands.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {
    
    private final ExpenseService expenseService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseDTO> createExpense(@Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.createExpense(request));
    }
    
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(expenseService.getExpensesByInstitution(institutionId));
    }
}

