package com.codeurjc.backend.model;
import java.time.LocalDate;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    private String statusName;
    private Double statusPrice;
    private String paidByName;
    private Double paidByPice;
    private String claimedBy;
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "tickettype_id")
    private TicketType ticketType;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;



    public Ticket(String statusName, String paidByName, Double paidByPice, String claimedBy, LocalDate date){
        this.statusName = statusName;
        this.paidByName = paidByName;
        this.paidByPice = paidByPice;
        this.date = date;
    }



    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getPaidByName() {
        return paidByName;
    }
    public void setPaidByName(String paidByName) {
        this.paidByName = paidByName;
    }
    public Double getPaidByPice() {
        return paidByPice;
    }
    public void setPaidByPice(Double paidByPice) {
        this.paidByPice = paidByPice;
    }
    public String getClaimedBy(){
        return this.claimedBy;
    }
    public void setClaimedBy(String claimedBy) {
        this.claimedBy = claimedBy;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDate(LocalDate date) {       
        this.date = date;
    }
    public Double getStatusPrice(){
        return this.statusPrice;
    }
    public void setStatusPrice(Double statusPrice) {       
        this.statusPrice = statusPrice;
    }

    public TicketType getTicketType() {
        return ticketType;
    }
    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
    

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
