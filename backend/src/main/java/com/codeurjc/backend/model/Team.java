package com.codeurjc.backend.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Team {


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();



    public Team(String name, byte[] profilePicture){
        this.name = name;

        this.accounts = new ArrayList<>();
        this.tickets = new ArrayList<>();
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public byte[] getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public List<Account> getAccounts(){
        return this.accounts;
    }
    public void setAccounts(List<Account> accounts){
        this.accounts = accounts;
    }
    public List<Ticket> getTickets(){
        return this.tickets;
    }
    public void setTickets(List<Ticket> tickets){
        this.tickets = tickets;
    }

}
