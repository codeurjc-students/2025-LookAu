package com.codeurjc.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long>{
    List<TicketType> findAllByTickectEnumType(EnumTickectType tickectEnumType);
    @SuppressWarnings("null")
    Optional<TicketType> findById(Long id);
}