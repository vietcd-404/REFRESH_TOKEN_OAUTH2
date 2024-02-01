package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserRole;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface RoleService {
    Optional<Role> findByRoleName(UserRole roleName);
}
