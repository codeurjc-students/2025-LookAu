package com.codeurjc.backend.model;

import java.util.*;

import jakarta.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "account_pending_friends", 
        joinColumns = @JoinColumn(name = "account_id"), 
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Account> pendingFriends = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "account_request_friends", 
        joinColumns = @JoinColumn(name = "account_id"), 
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Account> requestFriends = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "account_my_friends", 
        joinColumns = @JoinColumn(name = "account_id"), 
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Account> myFriends = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "account_teams", 
        joinColumns = @JoinColumn(name = "account_id"), 
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();


    public Account() {
    }

    public Account(String nickName, String firstName, String lastName, String email, String password, byte[] profilePicture){
        this.nickName = nickName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setPassword(password);
        this.profilePicture = profilePicture;

        this.myFriends = new ArrayList<>();
        this.pendingFriends = new ArrayList<>();
    }

    public Account(String nickName, String firstName, String lastName, String email, String password){
        this.nickName = nickName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setPassword(password);
        
        this.myFriends = new ArrayList<>();
        this.pendingFriends = new ArrayList<>();
    }



    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
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

    //ListFriends
    public List<Account> getMyFriends(){
        return this.myFriends;
    }
    public void setMyFriends(List<Account> myFriends){
        this.myFriends = myFriends;
    }
    public List<Account> getPendingFriends(){
        return this.pendingFriends;
    }
    public void setPendingFriends(List<Account> pendingFriends){
        this.pendingFriends = pendingFriends;
    }
    public List<Ticket> getTickets() {
        return this.tickets;
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    public List<Account> getRequestFriends() {
        return requestFriends;
    }
    public void setRequestFriends(List<Account> requestFriends) {
        this.requestFriends = requestFriends;
    }

    //nickNameList
    public List<String> getNickNamePendingFriends(){
        List<String> lNickNames = new ArrayList<String>();

        for(Account acc: this.pendingFriends){
            lNickNames.add(acc.nickName);
        }

        return lNickNames;
    }
    public List<String> getNickNameMyFriends(){
        List<String> lNickNames = new ArrayList<String>();

        for(Account acc: this.myFriends){
            lNickNames.add(acc.nickName);
        }

        return lNickNames;
    }
    public List<String> getNickNameRequestFriends(){
        List<String> lNickNames = new ArrayList<String>();

        for(Account acc: this.requestFriends){
            lNickNames.add(acc.nickName);
        }

        return lNickNames;
    }

}
