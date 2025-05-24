package com.codeurjc.backend.model.types;

import java.util.*;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EUROMILLONES")
public class Euromillones extends TicketType {

    private Integer num1;
    private Integer num2;
    private Integer num3;
    private Integer num4;
    private Integer num5;
    private Integer star1;
    private Integer star2;

    private EnumTickectType tickectType;

    public Euromillones(Integer num1, Integer num2, Integer num3, Integer num4, Integer num5, Integer star1, Integer star2){
        super(EnumTickectType.EUROMILLONES);
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.star1 = star1;
        this.star2 = star2;
    }

    public Euromillones(List<Integer> numbers){
        super(EnumTickectType.EUROMILLONES);
        this.num1 = numbers.get(0);
        this.num2 = numbers.get(1);
        this.num3 = numbers.get(2);
        this.num4 = numbers.get(3);
        this.num5 = numbers.get(4);
        this.star1 = numbers.get(5);
        this.star2 = numbers.get(6);
    }

    public Euromillones(){
        super(EnumTickectType.EUROMILLONES);
    }

    public List<Integer> getNumList(){
        return Arrays.asList(this.num1, this.num2, this.num3, this.num4, this.num5);
    }
    public List<Integer> getStarList(){
        return Arrays.asList(this.star1, this.star2);
    }

    public void setNumList(List<Integer> numbers){
        this.num1 = numbers.get(0);
        this.num2 = numbers.get(1);
        this.num3 = numbers.get(2);
        this.num4 = numbers.get(3);
        this.num5 = numbers.get(4);
    }

    public List<Integer> getStartList(){
        return Arrays.asList(this.star1, this.star2);
    }
    public void setStartList(List<Integer> numbers){
        this.star1 = numbers.get(0);
        this.star2 = numbers.get(1);
    }
    

    public String getTicketTypeName(){
        return "Euromillones";
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

    public Integer getStar2() {
        return star2;
    }

    public void setStar2(Integer star2) {
        this.star2 = star2;
    }

    public Integer getStar1() {
        return star1;
    }

    public void setStar1(Integer star1) {
        this.star1 = star1;
    }
}