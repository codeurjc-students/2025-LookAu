package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Eurodreams;

public interface EurodreamsRepository extends JpaRepository<Eurodreams, Long>{
    @SuppressWarnings("null")
    Optional<Eurodreams> findById(Long id);
    
}