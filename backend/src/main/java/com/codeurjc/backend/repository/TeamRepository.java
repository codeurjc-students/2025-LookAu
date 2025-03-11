package com.codeurjc.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{
    @SuppressWarnings("null")
    Optional<Team> findById(Long id);
    
}