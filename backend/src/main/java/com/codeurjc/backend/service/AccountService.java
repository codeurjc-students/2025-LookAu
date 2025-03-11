package com.codeurjc.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.repository.AccountRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> getByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public boolean emailRepeat(String email){
        List<Account> accs = accountRepository.findAll();
        
        for(Account acc: accs){
            if(acc.getEmail().equals(email)){
                return false;
            }
        }
        return true;
    }

    public void setAccount(Account acc){
        accountRepository.save(acc);
    }
}
