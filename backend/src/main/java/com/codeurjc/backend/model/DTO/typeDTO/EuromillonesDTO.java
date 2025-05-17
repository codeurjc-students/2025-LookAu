package com.codeurjc.backend.model.DTO.typeDTO;

public class EuromillonesDTO{

    private Long id;
    private Integer num1;
    private Integer num2;
    private Integer num3;
    private Integer num4;
    private Integer num5;
    private Integer star1;
    private Integer star2;


    public EuromillonesDTO(Long id, Integer num1, Integer num2, Integer num3, Integer num4, Integer num5, Integer star1, Integer star2){
        this.id = id;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.star1 = star1;
        this.star2 = star2;
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
    public Integer getStar1() {
        return star1;
    }
    public void setStar1(Integer star1) {
        this.star1 = star1;
    }
    public Integer getStar2() {
        return star2;
    }
    public void setStar2(Integer star2) {
        this.star2 = star2;
    }
}
