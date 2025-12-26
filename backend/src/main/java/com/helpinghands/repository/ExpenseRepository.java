package com.helpinghands.repository;

import com.helpinghands.entity.Expense;
import com.helpinghands.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByInstitution(Institution institution);
    List<Expense> findByInstitutionOrderByCreatedAtDesc(Institution institution);
}

