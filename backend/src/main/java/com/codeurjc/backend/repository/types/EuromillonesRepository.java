package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Euromillones;

public interface EuromillonesRepository extends JpaRepository<Euromillones, Long>{
    @SuppressWarnings("null")
    Optional<Euromillones> findById(Long id);    
}