package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repo.UsersRepo;
import com.example.demo.security.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Users users= usersRepo.findByUsername(username);
       if (users==null){
           throw new UsernameNotFoundException("User not found");
       }
       return CustomUserDetail.mapUserDetail(users);
    }
}
