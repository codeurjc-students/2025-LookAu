package com.codeurjc.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.TicketType;
import com.codeurjc.backend.repository.TicketTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TicketTypeService {


    @Autowired
    private TicketTypeRepository tickectTypeRepository;

    public Optional<TicketType> getById(Long id){
        return tickectTypeRepository.findById(id);
    }

    public void setTicketType(TicketType ticketType){
        tickectTypeRepository.save(ticketType);
    }

    public void setTicketTypes(List<TicketType> lTicketTypes){
        tickectTypeRepository.saveAll(lTicketTypes);
    }

    public void deleteTicketType(TicketType ticketType){
        tickectTypeRepository.delete(ticketType);
    }

    public TicketType getOne(){
        return tickectTypeRepository.findAll().get(0);
    }
}