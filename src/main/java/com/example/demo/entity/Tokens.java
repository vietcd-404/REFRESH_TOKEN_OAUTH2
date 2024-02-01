package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "Tokens")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_token;

    private String token;

    //Thời gian tạo ra token
    private LocalDateTime createdAt;

    //Thời gian token tồn tại
    private LocalDateTime expiresAt;

    //Thời gian xác nhận token
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "Users")
    private Users users;
}
