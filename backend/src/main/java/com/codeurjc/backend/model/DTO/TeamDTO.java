package com.codeurjc.backend.model.DTO;

import com.codeurjc.backend.model.Team;

public class TeamDTO {

    private Long id;
    private String name;
    private String[] friendsTeam;


    public TeamDTO(){}

    public TeamDTO(Long id, String name, String[] friendsTeam){
        this.id = id;
        this.name = name;
        this.friendsTeam = friendsTeam;
    }

    public TeamDTO(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public TeamDTO(Team team){
        this.id = team.getId();
        this.name = team.getName();
    }
    

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String[] getFriendsTeam() {
        return friendsTeam;
    }
    public void setFriendsTeam(String[] friendsTeam) {
        this.friendsTeam = friendsTeam;
    }
    
}
