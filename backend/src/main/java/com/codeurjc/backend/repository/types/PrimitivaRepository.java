package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Primitiva;

public interface PrimitivaRepository extends JpaRepository<Primitiva, Long>{
    @SuppressWarnings("null")
    Optional<Primitiva> findById(Long id);
    
}