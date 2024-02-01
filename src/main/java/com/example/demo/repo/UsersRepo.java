package com.example.demo.repo;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Users a " +
            "SET a.status = 1 WHERE a.username = ?1")
    int statusUsername(String username);
}
