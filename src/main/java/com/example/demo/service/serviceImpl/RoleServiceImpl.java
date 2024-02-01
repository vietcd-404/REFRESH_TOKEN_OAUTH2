package com.example.demo.service.serviceImpl;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserRole;
import com.example.demo.repo.RoleRepo;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo roleRepo;

    @Override
    public Optional<Role> findByRoleName(UserRole roleName) {
        return roleRepo.findByRoleName(roleName);
    }


}
