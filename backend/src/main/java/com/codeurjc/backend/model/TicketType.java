package com.codeurjc.backend.model;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private EnumTickectType tickectType;

    public TicketType(EnumTickectType tickectType){
        this.tickectType = tickectType;
    }

}
