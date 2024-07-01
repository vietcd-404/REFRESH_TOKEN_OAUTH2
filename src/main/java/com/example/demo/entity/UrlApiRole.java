package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "url_api_role")
@Entity
public class UrlApiRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int urlApiId;

    private  int roleId;
}
