package com.helpinghands.repository;

import com.helpinghands.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGuestId(String guestId);
    boolean existsByEmail(String email);
    boolean existsByGuestId(String guestId);
}

