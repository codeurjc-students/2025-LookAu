package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Loteria;

public interface LoteriaRepository extends JpaRepository<Loteria, Long>{
    @SuppressWarnings("null")
    Optional<Loteria> findById(Long id);    
}