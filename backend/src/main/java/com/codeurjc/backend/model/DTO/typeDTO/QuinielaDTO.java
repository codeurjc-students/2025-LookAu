package com.codeurjc.backend.model.DTO.typeDTO;

public class QuinielaDTO {
    
    private Long id;
    private String[] bet1;
    private String[] bet2;
    private String[] bet3;
    private String[] bet4;
    private String[] bet5;
    private String[] bet6;
    private String[] bet7;
    private String[] bet8;


    public QuinielaDTO(Long id, String[] bet1, String[] bet2, String[] bet3, String[] bet4, String[] bet5, String[] bet6, String[] bet7, String[] bet8) {
        this.id = id;
        this.bet1 = bet1;
        this.bet2 = bet2;
        this.bet3 = bet3;
        this.bet4 = bet4;
        this.bet5 = bet5;
        this.bet6 = bet6;
        this.bet7 = bet7;
        this.bet8 = bet8;
    }


    public QuinielaDTO(){
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String[] getBet7() {
        return bet7;
    }
    public void setBet7(String[] bet7) {
        this.bet7 = bet7;
    }
    public String[] getBet5() {
        return bet5;
    }
    public void setBet5(String[] bet5) {
        this.bet5 = bet5;
    }
    public String[] getBet4() {
        return bet4;
    }
    public void setBet4(String[] bet4) {
        this.bet4 = bet4;
    }
    public String[] getBet6() {
        return bet6;
    }
    public void setBet6(String[] bet6) {
        this.bet6 = bet6;
    }
     public String[] getBet8() {
        return bet8;
    }
    public void setBet8(String[] bet8) {
        this.bet8 = bet8;
    }

}
