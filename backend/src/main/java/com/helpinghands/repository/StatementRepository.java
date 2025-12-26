package com.helpinghands.repository;

import com.helpinghands.entity.Statement;
import com.helpinghands.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByDonation(Donation donation);
    List<Statement> findAllByOrderByCreatedAtDesc();
}

