package com.helpinghands.repository;

import com.helpinghands.entity.Cause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CauseRepository extends JpaRepository<Cause, Long> {
    List<Cause> findByActiveTrue();
    Optional<Cause> findByName(String name);
}

