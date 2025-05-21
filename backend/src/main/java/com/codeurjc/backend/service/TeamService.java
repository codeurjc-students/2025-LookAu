package com.codeurjc.backend.service;
import com.codeurjc.backend.security.CSRFHandlerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.DTO.AccountDTO;
import com.codeurjc.backend.model.DTO.TeamDTO;
import com.codeurjc.backend.model.DTO.TicketTeamDTO;
import com.codeurjc.backend.repository.AccountRepository;
import com.codeurjc.backend.repository.TeamRepository;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TeamRepository teamRepository;


    private AccountService accountService;

    TeamService(CSRFHandlerConfiguration CSRFHandlerConfiguration) {
    } 

    public Optional<Team> getById(Long id){
        return teamRepository.findById(id);
    }

    public void setTeam(Team team){
        teamRepository.save(team);
    }

    public void deleteTeam(Team team){
        teamRepository.delete(team);
    }

    public List<String> getAccountsTeam(Long id){
        Optional<Team> teamOpp = this.getById(id);

        if(teamOpp.isPresent()){
            return teamOpp.get().getAccounts().stream()
            .sorted(Comparator.comparing(Account::getNickName))
            .map(this::convertToString) 
            .collect(Collectors.toList());
        }else{
            return null;
        }
    }

    
    public List<TeamDTO> getSearchingTeams(String searchTerm, List<Team> lTeams) {

        if(!lTeams.isEmpty()){
            return lTeams.stream()
            .filter(item -> item.getName() != null && item.getName().toLowerCase().contains(searchTerm.toLowerCase()))
            .map(this::convertToTeamDTO)
            .collect(Collectors.toList());
        }else{
            return new ArrayList<TeamDTO>();
        }
    }

    
    public List<TicketTeamDTO> getAllTicketTeams(List<Ticket> lTeams) {

        if(!lTeams.isEmpty()){
            return lTeams.stream().map(this::convertToTicketTeamDTO).collect(Collectors.toList());
        }else{
            return new ArrayList<TicketTeamDTO>();
        }
    }


    /***********************/
    /******* TICKETS *******/
    /***********************/
    public Page<TicketTeamDTO> getTeamTicketsPaged(String teamId, Pageable pageable) {
        Long idTeamLong = Long.valueOf(teamId);
    
        return getById(idTeamLong)
            .map(team -> {
                List<Ticket> tickets = team.getTickets();
    
                //set pagination and order by date
                List<Ticket> pagedTickets = tickets.stream()
                    .sorted(Comparator.comparing(Ticket::getDate).reversed()) 
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .collect(Collectors.toList());
    
                List<TicketTeamDTO> dtoList = new ArrayList<>();

                for (Ticket ticket : pagedTickets) {
                    try {
                        TicketTeamDTO ticketTeamDTO = this.convertToTicketTeamDTO(ticket);
                        dtoList.add(ticketTeamDTO);  
                    } catch (Exception e) {
                        System.err.println("Error al convertir el ticket: " + ticket);
                        e.printStackTrace();
                    }
                }
    
                return new PageImpl<>(dtoList, pageable, tickets.size());
            })
            .orElse(new PageImpl<>(Collections.emptyList(), pageable, 0));
    }


    public Page<TicketTeamDTO> getTeamTicketsFilterPaged(String teamId, String date, String type, Pageable pageable) {
        Long idTeamLong = Long.valueOf(teamId);
    
        return getById(idTeamLong)
            .map(team -> {
                List<Ticket> tickets = team.getTickets();

                //filter by date or type
                List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> {
                    boolean matchesDate = date == null || date.isEmpty() || 
                        (ticket.getDate() != null && ticket.getDate().toString().contains(date));
                    
                    boolean matchesType = type == null || type.isEmpty() || 
                        (ticket.getTicketType() != null && 
                         ticket.getTicketType().getStringTickectType().equalsIgnoreCase(type.trim()));
                    
                    return matchesDate && matchesType;
                })
                .sorted(Comparator.comparing(Ticket::getDate).reversed())
                .collect(Collectors.toList());
    

                //set pagination and order by date
                List<Ticket> pagedTickets = filteredTickets.stream()
                    .sorted(Comparator.comparing(Ticket::getDate).reversed()) 
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .collect(Collectors.toList()
                );
    

                //convert to dto
                List<TicketTeamDTO> dtoList = pagedTickets.stream()
                    .map(this::convertToTicketTeamDTO)
                    .collect(Collectors.toList());
    
                return new PageImpl<>(dtoList, pageable, filteredTickets.size());
            })
            .orElse(new PageImpl<>(Collections.emptyList(), pageable, 0));
    }
    
    

    /********************/
    /******* HELP *******/
    /********************/

    private TeamDTO convertToTeamDTO(Team team) {
        return new TeamDTO(team.getId(), team.getName());
    }

    private TicketTeamDTO convertToTicketTeamDTO(Ticket ticket) {
        return new TicketTeamDTO(ticket);
    }

    private String convertToString(Account account) {
        return account.getNickName();
    }
    
}