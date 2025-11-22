package com.codeurjc.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.TicketType;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
    @SuppressWarnings("null")
    Optional<Ticket> findAllById(Long id);
    
    List<Ticket> findAllByIdIn(List<Long> ids);
    
    List<Ticket> findAllByAccountId(Long id);
    List<Ticket> findAllByAccount(Account account);

    List<Ticket> findAllByTeamId(Long id);
    List<Ticket> findAllByTeam(Team team);

    @Query("SELECT t FROM Ticket t WHERE t.statusName = :statusName AND t.date <= CURRENT_DATE")
    List<Ticket> findAllByStatusName(String statusName);

    List<Ticket> findAllByDate(LocalDate date);

    @Query("SELECT t FROM Ticket t WHERE t.ticketType = :ticketType")
    List<Ticket> findByTicketType(@Param("ticketType") TicketType ticketType);
}