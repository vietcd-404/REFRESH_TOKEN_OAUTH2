package com.example.demo.repo;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleName(UserRole roleName);

    List<String> findByRoleName(String role);
}
