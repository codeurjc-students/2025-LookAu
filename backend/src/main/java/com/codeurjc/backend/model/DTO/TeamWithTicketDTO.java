package com.codeurjc.backend.model.DTO;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;


public class TeamWithTicketDTO {

    //ticket
    private List<TicketTeamDTO> lTickets;

    //team
    private String name;
    private List<String> friendsTeam;


    public TeamWithTicketDTO(){}

    public TeamWithTicketDTO(Team team){
        
        //tickets
        TicketTeamDTO ticketDTO;
        for(Ticket ticket: team.getTickets()){
            ticketDTO = new TicketTeamDTO(ticket);
            this.lTickets.add(ticketDTO);
        }

        //team
        this.name = team.getName();
        this.friendsTeam = team.getAccounts().stream()
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(Account::getNickName))
            .map(this::convertToString) 
            .collect(Collectors.toList());
    }


    //team
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getFriendsTeam() {
        return friendsTeam;
    }
    public void setFriendsTeam(List<String> friendsTeam) {
        this.friendsTeam = friendsTeam;
    }


    //help
    private String convertToString(Account account) {
        return account.getNickName();
    }
    
    
} 