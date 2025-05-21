package com.codeurjc.backend.service;
import com.codeurjc.backend.security.CSRFHandlerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.TicketType;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.repository.AccountRepository;
import com.codeurjc.backend.repository.TicketRepository;
import com.codeurjc.backend.repository.TicketTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketTypeService {

    private final CSRFHandlerConfiguration CSRFHandlerConfiguration;

    @Autowired
    private TicketTypeRepository tickectTypeRepository;

    TicketTypeService(CSRFHandlerConfiguration CSRFHandlerConfiguration) {
        this.CSRFHandlerConfiguration = CSRFHandlerConfiguration;
    } 

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
}