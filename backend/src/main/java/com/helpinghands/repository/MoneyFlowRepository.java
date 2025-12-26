package com.helpinghands.repository;

import com.helpinghands.entity.MoneyFlow;
import com.helpinghands.entity.Donation;
import com.helpinghands.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyFlowRepository extends JpaRepository<MoneyFlow, Long> {
    List<MoneyFlow> findByDonation(Donation donation);
    List<MoneyFlow> findByExpense(Expense expense);
    
    @Query("SELECT mf FROM MoneyFlow mf WHERE mf.donation.user.id = :userId")
    List<MoneyFlow> findByUserId(Long userId);
}

