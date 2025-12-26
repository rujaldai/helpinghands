package com.helpinghands.repository;

import com.helpinghands.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    List<Institution> findByActiveTrue();
    Optional<Institution> findByIsHostCompanyTrue();
    List<Institution> findByIsHostCompanyTrueAndActiveTrue();
}

