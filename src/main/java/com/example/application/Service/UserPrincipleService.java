package com.example.application.Service;

import com.example.application.Entity.UserPrinciple;
import com.example.application.Entity.Users;
import com.example.application.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipleService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUserName(username).orElseThrow(
                ()-> new UsernameNotFoundException(username)
        );
                return new UserPrinciple(user);
    }
}
