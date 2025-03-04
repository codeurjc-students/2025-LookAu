package com.codeurjc.backend.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

@Service
public class RepositoryUserDetailService implements UserDetailsService {

    @Autowired
    @Lazy
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Busca el usuario por email
        Account acc = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Crea un UserDetails sin roles
        return new org.springframework.security.core.userdetails.User(acc.getEmail(), acc.getPassword(), new ArrayList<>());
    }
}