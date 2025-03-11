package com.codeurjc.backend.repository.types;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.types.Quintuple;

public interface QuintupleRepository extends JpaRepository<Quintuple, Long>{
    @SuppressWarnings("null")
    Optional<Quintuple> findById(Long id);
}