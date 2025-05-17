package com.codeurjc.backend.model.DTO.typeDTO;

public class LoteriaDTO{

    private Long id;
    private Integer number;
    private Integer euros;
    private Integer series;
    private Integer fraction;


    public LoteriaDTO(Long id, Integer number, Integer euros, Integer series, Integer fraction){
        this.id = id;
        this.number = number;
        this.euros = euros;
        this.series = series;
        this.fraction = fraction;
    }


    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public Integer getEuros() {
        return euros;
    }
    public void setEuros(Integer euros) {
        this.euros = euros;
    }
    public Integer getSeries() {
        return series;
    }
    public void setSeries(Integer series) {
        this.series = series;
    }
    public Integer getFraction() {
        return fraction;
    }
    public void setFraction(Integer fraction) {
        this.fraction = fraction;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
