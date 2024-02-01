package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String phone;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date created = new Date();
    private int status = 0;

    private Set<String> role;
}
