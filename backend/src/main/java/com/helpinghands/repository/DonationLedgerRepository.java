package com.helpinghands.repository;

import com.helpinghands.entity.DonationLedger;
import com.helpinghands.entity.Donation;
import com.helpinghands.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DonationLedgerRepository extends JpaRepository<DonationLedger, Long> {
    List<DonationLedger> findByDonation(Donation donation);
    List<DonationLedger> findByExpense(Expense expense);
    
    @Query("SELECT mf FROM DonationLedger mf WHERE mf.donation.user.id = :userId")
    List<DonationLedger> findByUserId(Long userId);

    @Query("SELECT SUM(dl.amount) from DonationLedger dl where dl.toInstitution = :institutionId")
    BigDecimal getTotalIncomingAmount(Long institutionId);

    @Query("SELECT SUM(dl.amount) from DonationLedger dl where dl.fromInstitution = :institutionId")
    BigDecimal getTotalOutgoingAmount(Long institutionId);
}

