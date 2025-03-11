package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Gordo;

public interface GordoRepository extends JpaRepository<Gordo, Long>{
    @SuppressWarnings("null")
    Optional<Gordo> findById(Long id);
    
}