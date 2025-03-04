package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.Account;

public interface GordoRepository extends JpaRepository<Account, Long>{
    Optional<Account> findFirstByFirstName(String name);
    @SuppressWarnings("null")
    Optional<Account> findById(Long id);
    Optional<Account> findByEmail(String email);
    
}