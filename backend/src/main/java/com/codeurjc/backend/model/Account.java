package com.codeurjc.backend.model;

import java.util.*;

import jakarta.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @ManyToMany
    @JoinTable(
        name = "account_pending_friends", 
        joinColumns = @JoinColumn(name = "account_id"), 
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Account> pendingFriends = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "account_my_friends", 
        joinColumns = @JoinColumn(name = "account_id"), 
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Account> myFriends = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    
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

}
