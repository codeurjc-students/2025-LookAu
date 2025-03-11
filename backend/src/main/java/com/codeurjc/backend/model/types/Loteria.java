package com.codeurjc.backend.model.types;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LOTERIA")
public class Loteria extends TicketType {

    private Integer number;
    private Integer euros;
    private Integer series;
    private Integer fraction;

    private EnumTickectType tickectType;

    public Loteria(Integer number, Integer euros, Integer series, Integer fraction){
        super(EnumTickectType.LOTERIA);
        this.number = number;
        this.euros = euros;
        this.series = series;
        this.fraction = fraction;
    }

    public Loteria(){
        super(EnumTickectType.LOTERIA);
    }

    public String getTicketTypeName(){
        return "Loteria Nacional";
    }
    public EnumTickectType getTickectType() {
        return tickectType;
    }

    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getEuros() {
        return euros;
    }
    public void setEuros(Integer euros) {
        this.euros = euros;
    }

    public Integer getSeries() {
        return series;
    }
    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getFraction() {
        return fraction;
    }
    public void setFraction(Integer fraction) {
        this.fraction = fraction;
    }
}
