package com.example.demo.service;

import com.example.demo.entity.Users;

import org.springframework.stereotype.Service;


public interface UserService {
    Users findByUsername(String username);
    Boolean  existByUsername(String username);
    Boolean  existByEmail(String email);
    Users saveOrUpdate(Users users);
}
