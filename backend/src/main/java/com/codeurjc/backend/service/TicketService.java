package com.codeurjc.backend.service;
import com.codeurjc.backend.security.CSRFHandlerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.repository.AccountRepository;
import com.codeurjc.backend.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final CSRFHandlerConfiguration CSRFHandlerConfiguration;

    @Autowired
    private TicketRepository tickectRepository;

    TicketService(CSRFHandlerConfiguration CSRFHandlerConfiguration) {
        this.CSRFHandlerConfiguration = CSRFHandlerConfiguration;
    } 

    public Optional<Ticket> getById(Long id){
        return tickectRepository.findById(id);
    }

    public void setTicket(Ticket ticket){
        tickectRepository.save(ticket);
    }

    public void setTickets(List<Ticket> lTickets){
        tickectRepository.saveAll(lTickets);
    }

    public void deleteTicket(Ticket ticket){
        tickectRepository.delete(ticket);
    }
}