package com.codeurjc.backend.model.DTO.typeDTO;

public class GordoDTO{

    private Long id;
    private Integer num1;
    private Integer num2;
    private Integer num3;
    private Integer num4;
    private Integer num5;
    private Integer key;


    public GordoDTO(Long id, Integer num1, Integer num2, Integer num3, Integer num4, Integer num5, Integer key){
        this.id = id;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.key = key;
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getKey() {
        return key;
    }
    public void setKey(Integer key) {
        this.key = key;
    }
}
