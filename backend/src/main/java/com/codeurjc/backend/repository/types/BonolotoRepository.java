package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Bonoloto;

public interface BonolotoRepository extends JpaRepository<Bonoloto, Long>{
    @SuppressWarnings("null")
    Optional<Bonoloto> findById(Long id);
    
}