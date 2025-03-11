package com.codeurjc.backend.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "ticket_type", discriminatorType = DiscriminatorType.STRING) 
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EnumTickectType tickectEnumType;

    public EnumTickectType getTickectType() {
        return tickectEnumType;
    }

    public void setTickectType(EnumTickectType tickectEnumType) {
        this.tickectEnumType = tickectEnumType;
    }

    public TicketType(EnumTickectType tickectEnumType){
        this.tickectEnumType = tickectEnumType;
    }

    public TicketType() {
    }

}
