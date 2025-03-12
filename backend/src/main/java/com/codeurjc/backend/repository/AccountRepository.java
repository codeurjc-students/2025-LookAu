package com.codeurjc.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.Account;


public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findFirstByFirstName(String name);
    @SuppressWarnings("null")
    Optional<Account> findById(Long id);
    Optional<Account> findByEmail(String email);

    List<Account> findByNickNameContainingIgnoreCase(String nickName);
    List<Account> findByNickName(String nickName);
    
}