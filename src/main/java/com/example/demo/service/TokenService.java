package com.example.demo.service;

import com.example.demo.entity.Tokens;
import com.example.demo.repo.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    private TokenRepo tokenRepo;

    public void saveToken(Tokens tokens){
        tokenRepo.save(tokens);
    }
    public Optional<Tokens> getToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return tokenRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
