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

    private String ticketStringType;

    public TicketType(EnumTickectType tickectEnumType){
        this.tickectEnumType = tickectEnumType;
        switch (tickectEnumType) {
            case BONOLOTO:
                this.ticketStringType = "Bonoloto";
                break;
            case EURODREAMS:
                this.ticketStringType = "Eurodreams";
                break;
            case EUROMILLONES:
                this.ticketStringType = "Euromillones";
                break;
            case GORDO:
                this.ticketStringType = "El Gordo";
                break;
            case LOTERIA:
                this.ticketStringType = "Lotería Nacional";
                break;
            case LOTOTURF:
                this.ticketStringType = "Lototurf";
                break;
            case PRIMITIVA:
                this.ticketStringType = "La Primitiva";
                break;
            case QUINIELA:
                this.ticketStringType = "La Quiniela";
                break;
            case QUINIGOL:
                this.ticketStringType = "El Quinigol";
                break;
            case QUINTUPLE:
                this.ticketStringType = "Quíntuple plus";
                break;
            
        }
    }

    public TicketType() {
        
    }


    public EnumTickectType getTickectType() {
        return tickectEnumType;
    }
    public void setTickectType(EnumTickectType tickectEnumType) {
        this.tickectEnumType = tickectEnumType;
    }
    public String getStringTickectType() {
        return ticketStringType;
    }
    public void setStringTickectType(String ticketStringType) {
        this.ticketStringType = ticketStringType;
    }
    public Long getId() {
        return id;
    }

}
