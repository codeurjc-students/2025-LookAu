package com.codeurjc.backend.model.TicketsTypes;

import java.util.*;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

public class Loteria extends TicketType {

    private Double number;
    private Double euros;
    private Double series;
    private Double fraction;

    private EnumTickectType tickectType;

    public Loteria(Double number, Double euros, Double series, Double fraction){
        super(EnumTickectType.LOTERIA);
        this.number = number;
        this.euros = euros;
        this.series = series;
        this.fraction = fraction;
    }

    public String getTicketTypeName(){
        return "Loteria Nacional";
    }
    public EnumTickectType getTickectType() {
        return tickectType;
    }

    public Double getNumber() {
        return number;
    }
    public void setNumber(Double number) {
        this.number = number;
    }

    public Double getEuros() {
        return euros;
    }
    public void setEuros(Double euros) {
        this.euros = euros;
    }

    public Double getSeries() {
        return series;
    }
    public void setSeries(Double series) {
        this.series = series;
    }

    public Double getFraction() {
        return fraction;
    }
    public void setFraction(Double fraction) {
        this.fraction = fraction;
    }
}
