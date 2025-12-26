package com.helpinghands.repository;

import com.helpinghands.entity.Donation;
import com.helpinghands.entity.Institution;
import com.helpinghands.entity.Cause;
import com.helpinghands.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUser(User user);
    List<Donation> findByInstitution(Institution institution);
    List<Donation> findByCause(Cause cause);
    
    @Query("SELECT d.user, SUM(d.amount) as total FROM Donation d GROUP BY d.user ORDER BY total DESC")
    List<Object[]> findTopDonators();
    
    @Query("SELECT d.institution, SUM(d.amount) as total FROM Donation d WHERE d.institution IS NOT NULL GROUP BY d.institution ORDER BY total DESC")
    List<Object[]> findTopInstitutions();
    
    @Query("SELECT d.cause, SUM(d.amount) as total FROM Donation d WHERE d.cause IS NOT NULL GROUP BY d.cause ORDER BY total DESC")
    List<Object[]> findTopCauses();
}

