package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Quinigol;

public interface QuinigolRepository extends JpaRepository<Quinigol, Long>{
    @SuppressWarnings("null")
    Optional<Quinigol> findById(Long id);    
}