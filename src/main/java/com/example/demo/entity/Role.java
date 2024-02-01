package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "Role")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private UserRole roleName;
}
