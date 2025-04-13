package com.codeurjc.backend.model.DTO;

import com.codeurjc.backend.model.Account;

public class RegisterAccountDTO {

    private String nickName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;


    public RegisterAccountDTO(){}

    public RegisterAccountDTO(Account acc) {
        this.nickName = acc.getNickName();
        this.firstName = acc.getFirstName();
        this.lastName = acc.getLastName();
        this.email = acc.getEmail();
        this.password = acc.getPassword();
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
}