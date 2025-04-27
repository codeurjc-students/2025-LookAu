package com.codeurjc.backend.model.DTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.codeurjc.backend.model.Ticket;

public class TicketTeamDTO {

    private Long id;
    private String type;
    private String date;
    private String statusName;
    private String statusPrice;
    private String claimedBy;
    private String paidByName;
    private String paidByPice;  //also use for the total of the reimbursement

    private Boolean balanced;   //helper var

    private List<String> idAccountsAreBeingPaid;



    public TicketTeamDTO(){}

    public TicketTeamDTO(Ticket ticket){
        this.id = ticket.getId();
        this.date = ticket.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.type = ticket.getTicketType()!=null ? ticket.getTicketType().getStringTickectType() : "";
        this.statusName = ticket.getStatusName();
        this.statusPrice = String.valueOf(ticket.getStatusPrice());

        this.claimedBy = ticket.getClaimedBy();
        this.paidByName = ticket.getPaidByName();
        this.paidByPice = String.valueOf(ticket.getPaidByPice());
        this.balanced = ticket.isBalancedTicket();
        if (ticket.getIdAccountsAreBeingPaid() != null && !ticket.getIdAccountsAreBeingPaid().isEmpty()) {
            this.idAccountsAreBeingPaid = ticket.getIdAccountsAreBeingPaid().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        }
        String lleé;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getStatusPrice() {
        return statusPrice;
    }
    public void setStatusPrice(String statusPrice) {
        this.statusPrice = statusPrice;
    }
    public String getClaimedBy() {
        return claimedBy;
    }
    public void setClaimedBy(String claimedBy) {
        this.claimedBy = claimedBy;
    }
    public String getPaidByName() {
        return paidByName;
    }
    public void setPaidByName(String paidByName) {
        this.paidByName = paidByName;
    }
    public String getPaidByPice() {
        return paidByPice;
    }
    public void setPaidByPice(String paidByPice) {
        this.paidByPice = paidByPice;
    }
    public Boolean getBalanced() {
        return balanced;
    }
    public void setBalanced(Boolean balanced) {
        this.balanced = balanced;
    }
    public List<String> getIdAccountsAreBeingPaid() {
        return idAccountsAreBeingPaid;
    }
    public void setIdAccountsAreBeingPaid(List<String> idAccountsAreBeingPaid) {
        this.idAccountsAreBeingPaid = idAccountsAreBeingPaid;
    }
}
