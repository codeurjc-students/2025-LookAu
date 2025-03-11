package com.codeurjc.backend.model.types;

import java.util.Arrays;
import java.util.List;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("QUINIGOL")
public class Quinigol extends TicketType {

    //values: 0, 1, 2, M
    private String[] bet1 = new String[2];      
    private String[] bet2 = new String[2];
    private String[] bet3 = new String[2];
    private String[] bet4 = new String[2];
    private String[] bet5 = new String[2];
    private String[] bet6 = new String[2];

    private EnumTickectType tickectType;

    public Quinigol() {
        super(EnumTickectType.QUINIGOL);
    }

    public Quinigol(String[] bet1, String[] bet2, String[] bet3, String[] bet4, String[] bet5, String[] bet6, String[] bet7, String[] bet8) {
        super(EnumTickectType.QUINIGOL);
        this.bet1 = bet1;
        this.bet2 = bet2;
        this.bet3 = bet3;
        this.bet4 = bet4;
        this.bet5 = bet5;
        this.bet6 = bet6;
    }

    public Quinigol(List<String> bet1, List<String> bet2, List<String> bet3, List<String> bet4, List<String> bet5, List<String> bet6, List<String> bet7, List<String> bet8) {
        super(EnumTickectType.QUINIGOL);
        this.bet1 = bet1.toArray(new String[0]);
        this.bet2 = bet2.toArray(new String[0]);
        this.bet3 = bet3.toArray(new String[0]);
        this.bet4 = bet4.toArray(new String[0]);
        this.bet5 = bet5.toArray(new String[0]);
        this.bet6 = bet6.toArray(new String[0]);
    }

    public String getTicketTypeName() {
        return "El Quinigol";
    }

    public EnumTickectType getTickectType() {
        return tickectType;
    }

    public String[] getBet1() {
        return bet1;
    }

    public void setBet1(String[] bet1) {
        this.bet1 = bet1;
    }

    public String[] getBet2() {
        return bet2;
    }

    public void setBet2(String[] bet2) {
        this.bet2 = bet2;
    }

    public String[] getBet3() {
        return bet3;
    }

    public void setBet3(String[] bet3) {
        this.bet3 = bet3;
    }

    public String[] getBet4() {
        return bet4;
    }

    public void setBet4(String[] bet4) {
        this.bet4 = bet4;
    }

    public String[] getBet5() {
        return bet5;
    }

    public void setBet5(String[] bet5) {
        this.bet5 = bet5;
    }

    public String[] getBet6() {
        return bet6;
    }

    public void setBet6(String[] bet6) {
        this.bet6 = bet6;
    }


    public List<String> getBet1List() {
        return Arrays.asList(bet1);
    }

    public void setBet1List(List<String> bett) {
        this.bet1 = bett.toArray(new String[0]);
    }

    public List<String> getBet2List() {
        return Arrays.asList(bet2);
    }

    public void setBet2List(List<String> bett) {
        this.bet2 = bett.toArray(new String[0]);
    }

    public List<String> getBet3List() {
        return Arrays.asList(bet3);
    }

    public void setBet3List(List<String> bett) {
        this.bet3 = bett.toArray(new String[0]);
    }

    public List<String> getBet4List() {
        return Arrays.asList(bet4);
    }

    public void setBet4List(List<String> bett) {
        this.bet4 = bett.toArray(new String[0]);
    }

    public List<String> getBet5List() {
        return Arrays.asList(bet5);
    }

    public void setBet5List(List<String> bett) {
        this.bet5 = bett.toArray(new String[0]);
    }

    public List<String> getBet6List() {
        return Arrays.asList(bet6);
    }

    public void setBet6List(List<String> bett) {
        this.bet6 = bett.toArray(new String[0]);
    }
}