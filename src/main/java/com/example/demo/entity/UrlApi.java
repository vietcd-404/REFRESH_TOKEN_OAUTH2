package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "url_api")
@Entity
public class UrlApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nameUrl;
}
