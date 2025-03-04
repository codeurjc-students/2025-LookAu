package com.codeurjc.backend.model.TicketsTypes;

import java.util.*;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

public class Eurodreams extends TicketType {

    private Double num1;
    private Double num2;
    private Double num3;
    private Double num4;
    private Double num5;
    private Double num6;
    private Double dream;

    private EnumTickectType tickectType;

    public Eurodreams(Double num1, Double num2, Double num3, Double num4, Double num5, Double num6, Double dream){
        super(EnumTickectType.EURODREAMS);
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.num6 = num6;
        this.dream = dream;
    }

    public String getTicketTypeName(){
        return "Eurodreams";
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

    public Double getNum6() {
        return num6;
    }

    public void setNum6(Double num6) {
        this.num6 = num6;
    }

    public Double getDream() {
        return dream;
    }

    public void setDream(Double dream) {
        this.dream = dream;
    }
}