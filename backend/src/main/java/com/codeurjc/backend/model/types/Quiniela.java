package com.codeurjc.backend.model.types;

import java.util.Arrays;
import java.util.List;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("QUINIELA")
public class Quiniela extends TicketType {

    //values: 0, 1, 2, M, X
    private String[] bet1 = new String[15];
    private String[] bet2 = new String[15];
    private String[] bet3 = new String[15];
    private String[] bet4 = new String[15];
    private String[] bet5 = new String[15];
    private String[] bet6 = new String[15];
    private String[] bet7 = new String[15];
    private String[] bet8 = new String[15];

    private EnumTickectType tickectType;

    public Quiniela() {
        super(EnumTickectType.QUINIELA);
    }

    public Quiniela(String[] bet1, String[] bet2, String[] bet3, String[] bet4, String[] bet5, String[] bet6, String[] bet7, String[] bet8) {
        super(EnumTickectType.QUINIELA);
        this.bet1 = bet1;
        this.bet2 = bet2;
        this.bet3 = bet3;
        this.bet4 = bet4;
        this.bet5 = bet5;
        this.bet6 = bet6;
        this.bet7 = bet7;
        this.bet8 = bet8;
    }

    public Quiniela(List<String> bet1, List<String> bet2, List<String> bet3, List<String> bet4, List<String> bet5, List<String> bet6, List<String> bet7, List<String> bet8) {
        super(EnumTickectType.QUINIELA);
        this.bet1 = bet1.toArray(new String[0]);
        this.bet2 = bet2.toArray(new String[0]);
        this.bet3 = bet3.toArray(new String[0]);
        this.bet4 = bet4.toArray(new String[0]);
        this.bet5 = bet5.toArray(new String[0]);
        this.bet6 = bet6.toArray(new String[0]);
        this.bet7 = bet7.toArray(new String[0]);
        this.bet8 = bet8.toArray(new String[0]);
    }

    public String getTicketTypeName() {
        return "La Quiniela";
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

    public String[] getBet7() {
        return bet7;
    }

    public void setBet7(String[] bet7) {
        this.bet7 = bet7;
    }

    public String[] getBet8() {
        return bet8;
    }

    public void setBet8(String[] bet8) {
        this.bet8 = bet8;
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

    public List<String> getBet7List() {
        return Arrays.asList(bet7);
    }

    public void setBet7List(List<String> bett) {
        this.bet7 = bett.toArray(new String[0]);
    }

    public List<String> getBet8List() {
        return Arrays.asList(bet8);
    }

    public void setBet8List(List<String> bett) {
        this.bet8 = bett.toArray(new String[0]);
    }
}