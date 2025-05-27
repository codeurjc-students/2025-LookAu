package com.codeurjc.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {


    @Autowired
    private TicketRepository tickectRepository;

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

    public Ticket getOne(){
        return tickectRepository.findAll().get(0);
    }
    
}