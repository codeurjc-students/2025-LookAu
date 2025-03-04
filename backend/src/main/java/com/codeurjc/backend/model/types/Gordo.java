package com.codeurjc.backend.model.TicketsTypes;

import java.util.*;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

public class Gordo extends TicketType {

    private Double num1;
    private Double num2;
    private Double num3;
    private Double num4;
    private Double num5;
    private Double key;

    private EnumTickectType tickectType;

    public Gordo(Double num1, Double num2, Double num3, Double num4, Double num5, Double key){
        super(EnumTickectType.GORDO);
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.key = key;
    }

    public String getTicketTypeName(){
        return "El Gordo";
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

    public Double getKey() {
        return key;
    }

    public void setKey(Double key) {
        this.key = key;
    }
}