package com.codeurjc.backend.model.types;

import java.util.*;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GORDO")
public class Gordo extends TicketType {

    private Integer num1;
    private Integer num2;
    private Integer num3;
    private Integer num4;
    private Integer num5;

    @Column(name = "`key`")
    private Integer key;

    private EnumTickectType tickectType;

    public Gordo(Integer num1, Integer num2, Integer num3, Integer num4, Integer num5, Integer key){
        super(EnumTickectType.GORDO);
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.key = key;
    }

    public Gordo(List<Integer> numbers){
        super(EnumTickectType.GORDO);
        this.num1 = numbers.get(0);
        this.num2 = numbers.get(1);
        this.num3 = numbers.get(2);
        this.num4 = numbers.get(3);
        this.num5 = numbers.get(4);
        this.key = numbers.get(5);
    }

    public Gordo(){
        super(EnumTickectType.GORDO);
    }

    public List<Integer> getNumList(){
        return Arrays.asList(this.num1, this.num2, this.num3, this.num4, this.num5);
    }
    public void setNumList(List<Integer> numbers){
        this.num1 = numbers.get(0);
        this.num2 = numbers.get(1);
        this.num3 = numbers.get(2);
        this.num4 = numbers.get(3);
        this.num5 = numbers.get(4);
    }

    public String getTicketTypeName(){
        return "El Gordo";
    }

    public EnumTickectType getTickectType() {
        return tickectType;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    public Integer getNum3() {
        return num3;
    }

    public void setNum3(Integer num3) {
        this.num3 = num3;
    }

    public Integer getNum4() {
        return num4;
    }

    public void setNum4(Integer num4) {
        this.num4 = num4;
    }

    public Integer getNum5() {
        return num5;
    }

    public void setNum5(Integer num5) {
        this.num5 = num5;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}