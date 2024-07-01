package com.example.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin("*")
@RestController()
@RequestMapping("/users")
public class UsersApi {
    @GetMapping()
    public ResponseEntity<?> user(){
        return ResponseEntity.ok(Map.of("message", "Đây là user đây"));
    }
}
