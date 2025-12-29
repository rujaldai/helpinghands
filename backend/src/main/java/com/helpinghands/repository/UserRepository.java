package com.helpinghands.repository;

import com.helpinghands.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGuestId(String guestId);
    boolean existsByEmail(String email);
    boolean existsByGuestId(String guestId);

    @Query("SELECT u from User u where u.active = true and u.role = com.helpinghands.entity.User.Role.ADMIN")
    List<User> findAdminUsers();

}

