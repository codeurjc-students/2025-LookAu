package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Quiniela;

public interface QuinielaRepository extends JpaRepository<Quiniela, Long>{
    @SuppressWarnings("null")
    Optional<Quiniela> findById(Long id);
    
}