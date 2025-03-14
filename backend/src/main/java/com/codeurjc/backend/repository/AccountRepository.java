package com.codeurjc.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeurjc.backend.model.Account;


public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findFirstByFirstName(String name);
    @SuppressWarnings("null")
    Optional<Account> findById(Long id);
    Optional<Account> findByEmail(String email);

    List<Account> findByNickNameContainingIgnoreCase(String nickName);
    List<Account> findByNickName(String nickName);

    @Query("SELECT a FROM Account acc JOIN acc.myFriends a WHERE acc.nickName = :nickName")
    Page<Account> findAllMyFriends(@Param("nickName") String nickName, Pageable pageable);

    @Query("SELECT a FROM Account acc JOIN acc.pendingFriends a WHERE acc.nickName = :nickName")
    Page<Account> findAllPendingFriends(@Param("nickName") String nickName, Pageable pageable);

    
    
}