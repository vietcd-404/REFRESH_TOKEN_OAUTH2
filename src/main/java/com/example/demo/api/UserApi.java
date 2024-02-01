package com.example.demo.api;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.email.EmailSender;
import com.example.demo.entity.Role;
import com.example.demo.entity.Tokens;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.Users;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.security.CustomUserDetail;
import com.example.demo.service.RoleService;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import com.example.demo.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth/")
public class UserApi {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailSender emailSender;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {

//        if (userService.existByUsername(signupRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại"));
//        }
//        if (userService.existByEmail(signupRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Email đã tồn tại"));
//        }
////        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
////        emailSender.send(
////                signupRequest.getEmail(),
////                buildEmail(request.getFirstName(), link));
//
//        Users users = new Users();
//        users.setUsername(signupRequest.getUsername());
//        users.setEmail(signupRequest.getEmail());
//        users.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
//        users.setStatus(signupRequest.getStatus());
//        users.setPhone(signupRequest.getPhone());
//        users.setCreated(signupRequest.getCreated());
//        Set<String> strSet = signupRequest.getRole();
//        Set<Role> listRole = new HashSet<>();
//
//
//        if (strSet == null) {
//            Role userRole = roleService.findByRoleName(UserRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Role is not found"));
//            listRole.add(userRole);
//        } else {
//            strSet.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleService.findByRoleName(UserRole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Role is not found"));
//                        listRole.add(adminRole);
//                    case "user":
//                        Role userRole = roleService.findByRoleName(UserRole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Role is not found"));
//                        listRole.add(userRole);
//                }
//            });
//        }
//        users.setRoles(listRole);
//        userService.saveOrUpdate(users);
//        String token = UUID.randomUUID().toString();
//
//        Tokens confirmationToken = new Tokens();
//        confirmationToken.setToken(token);
//        confirmationToken.setCreatedAt(LocalDateTime.now());
//        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
//        confirmationToken.setUsers(users);
//        tokenService.saveToken(confirmationToken);
//        System.out.println(token);
////        return ;
//        // TODO: Gửi mail để xác nhận
//        return ResponseEntity.badRequest().body(new MessageResponse(token));
        return userService.register(signupRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest){
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        //Sinh ra JWT trả vê client
        String token = jwtTokenProvider.genToken(customUserDetail);

        //Lấy các quyền của user
        List<String> listRole = customUserDetail.getAuthorities().stream().
                map(item->item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(token,customUserDetail.getUsername(),customUserDetail.getEmail(),
                customUserDetail.getPhone(),listRole));
    }

    @GetMapping( "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }
    
}
