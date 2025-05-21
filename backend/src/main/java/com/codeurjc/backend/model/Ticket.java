package com.codeurjc.backend.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String statusName;  //Winning - Not Winning - Pending - Reimbursement
    private Double statusPrice;     //not used for Reimbursement
    private String paidByName;  //also use for the account who owes to another account for reimbursement
    private Double paidByPice;  //also use for the total of the reimbursement
    private String claimedBy;   //also used for the account to be reimbursed
    private LocalDate date;     
    private Boolean isBalanced;  

    private List<Long> idAccountsAreBeingPaid;   //also use for idTicketReimbursement when status = Reimbursement. This indicates the tickets who are implicates in the Reimbursement
    private List<Long> idReimbursementAreReferenced;   


    @ManyToOne
    @JoinColumn(name = "tickettype_id", nullable = true)
    private TicketType ticketType;

    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id")
    private Account account;


    public Ticket(String statusName, String paidByName, Double paidByPice, String claimedBy, LocalDate date){
        this.claimedBy = claimedBy;
        this.statusName = statusName;
        this.paidByName = paidByName;
        this.paidByPice = paidByPice;
        this.date = date;
        this.isBalanced = false;
        this.idAccountsAreBeingPaid = new ArrayList<Long>();
        if(this.statusName.equals("Winning"))
            this.statusPrice = 100.00;
        else if(this.statusName.equals("Pending"))
            this.statusPrice = 0.00;
        else
            this.statusPrice = -10.00;
    }

    public Ticket(String statusName, String paidByName, Double paidByPice, String claimedBy, LocalDate date, List<Long> idTicketReimbursement){
        this.claimedBy = claimedBy;
        this.statusName = statusName;
        this.paidByName = paidByName;
        this.paidByPice = paidByPice;
        this.date = date;
        this.isBalanced = false;
        this.idAccountsAreBeingPaid = new ArrayList<Long>();
        if(this.statusName.equals("Winning"))
            this.statusPrice = 100.00;
        else if(this.statusName.equals("Pending"))
            this.statusPrice = 0.00;
        else
            this.statusPrice = -10.00;
    }

    public Ticket() {
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
    public Boolean isBalancedTicket(){
        return this.isBalanced;
    }
    public void setBalancedTicket(Boolean isBalanced) {       
        this.isBalanced = isBalanced;
    }

    public List<Long> getIdAccountsAreBeingPaid(){
        return this.idAccountsAreBeingPaid;
    }
    public void setIdAccountsAreBeingPaid(List<Long> idAccountsAreBeingPaid) {       
        this.idAccountsAreBeingPaid = idAccountsAreBeingPaid;
    }
    public List<Long> getIdReimbursementAreReferenced() {
        return idReimbursementAreReferenced;
    }
    public void setIdReimbursementAreReferenced(List<Long> idReimbursementAreReferenced) {
        this.idReimbursementAreReferenced = idReimbursementAreReferenced;
    }


    public TicketType getTicketType() {
        return ticketType;
    }
    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
