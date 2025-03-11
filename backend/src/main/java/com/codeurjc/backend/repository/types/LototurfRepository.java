package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Lototurf;

public interface LototurfRepository extends JpaRepository<Lototurf, Long>{
    @SuppressWarnings("null")
    Optional<Lototurf> findById(Long id);
    
}