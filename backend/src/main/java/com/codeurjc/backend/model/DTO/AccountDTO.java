package com.codeurjc.backend.model.DTO;

import java.util.ArrayList;
import java.util.List;

import com.codeurjc.backend.model.Account;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDTO {

    private Long id;
    private String nickName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private byte[] profilePicture;

    private List<String> pendingFriends = new ArrayList<>();
    private List<String> requestFriends = new ArrayList<>();
    private List<String> myFriends = new ArrayList<>();

    //private List<TeamDTO> teams = new ArrayList<>();
    //private List<TicketDTO> tickets = new ArrayList<>();

    public AccountDTO(){}

    public AccountDTO(Account acc) {
        //this.id = acc.getId();
        this.nickName = acc.getNickName();
        this.firstName = acc.getFirstName();
        this.lastName = acc.getLastName();
        this.email = acc.getEmail();
        this.password = acc.getPassword();

        this.profilePicture = acc.getProfilePicture();

        this.pendingFriends = acc.getNickNamePendingFriends();
        this.requestFriends = acc.getNickNameRequestFriends();
        this.myFriends = acc.getNickNameMyFriends();
    } 

    @JsonCreator
    public AccountDTO(
            @JsonProperty("nickName") String nickName,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password) {
        this.nickName = nickName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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
        this.password = password;
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
    public List<String> getMyFriends(){
        return this.myFriends;
    }
    public void setMyFriends(List<String> myFriends){
        this.myFriends = myFriends;
    }
    public List<String> getPendingFriends(){
        return this.pendingFriends;
    }
    public void setPendingFriends(List<String> pendingFriends){
        this.pendingFriends = pendingFriends;
    }
    public List<String> getRequestFriends() {
        return requestFriends;
    }
    public void setRequestFriends(List<String> requestFriends) {
        this.requestFriends = requestFriends;
    }
    
}