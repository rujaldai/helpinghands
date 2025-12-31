package com.helpinghands.controller;

import com.helpinghands.dto.ExpenseDTO;
import com.helpinghands.dto.ExpenseRequest;
import com.helpinghands.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private final ExpenseService expenseService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseDTO> createExpense(@Valid @RequestBody ExpenseRequest request) {
        logger.info("Creating new expense for institution: {}", request.getRecipientInstitutionId());
        ExpenseDTO expense = expenseService.createExpense(request);
        logger.info("Successfully created expense with ID: {}", expense.getId());
        return ResponseEntity.ok(expense);
    }
    
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        logger.info("Fetching all expenses");
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        logger.debug("Retrieved {} expenses", expenses.size());
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByInstitution(@PathVariable Long institutionId) {
        logger.info("Fetching expenses for institution ID: {}", institutionId);
        List<ExpenseDTO> expenses = expenseService.getExpensesByInstitution(institutionId);
        logger.debug("Retrieved {} expenses for institution ID: {}", expenses.size(), institutionId);
        return ResponseEntity.ok(expenses);
    }
}

