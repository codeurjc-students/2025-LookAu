package com.codeurjc.backend.model.TicketsTypes;

import java.util.*;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

public class Euromillones extends TicketType {

    private Double num1;
    private Double num2;
    private Double num3;
    private Double num4;
    private Double num5;
    private Double star1;
    private Double star2;

    private EnumTickectType tickectType;

    public Euromillones(Double num1, Double num2, Double num3, Double num4, Double num5, Double star1, Double star2){
        super(EnumTickectType.EUROMILLONES);
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.star1 = star1;
        this.star2 = star2;
    }

    public String getTicketTypeName(){
        return "Euromillones";
    }

    public EnumTickectType getTickectType() {
        return tickectType;
    }

    public Double getNum1() {
        return num1;
    }

    public void setNum1(Double num1) {
        this.num1 = num1;
    }

    public Double getNum2() {
        return num2;
    }

    public void setNum2(Double num2) {
        this.num2 = num2;
    }

    public Double getNum3() {
        return num3;
    }

    public void setNum3(Double num3) {
        this.num3 = num3;
    }

    public Double getNum4() {
        return num4;
    }

    public void setNum4(Double num4) {
        this.num4 = num4;
    }

    public Double getNum5() {
        return num5;
    }

    public void setNum5(Double num5) {
        this.num5 = num5;
    }

    public Double getStar2() {
        return star2;
    }

    public void setStar2(Double star2) {
        this.star2 = star2;
    }

    public Double getStar1() {
        return star1;
    }

    public void setStar1(Double star1) {
        this.star1 = star1;
    }
}