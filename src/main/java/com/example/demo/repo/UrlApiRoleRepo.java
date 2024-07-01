package com.example.demo.repo;

import com.example.demo.entity.UrlApiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlApiRoleRepo extends JpaRepository<UrlApiRole, Integer> {
}
