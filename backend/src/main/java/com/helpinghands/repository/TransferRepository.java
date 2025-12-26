package com.helpinghands.repository;

import com.helpinghands.entity.Transfer;
import com.helpinghands.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByFromInstitution(Institution institution);
    List<Transfer> findByToInstitution(Institution institution);
}

