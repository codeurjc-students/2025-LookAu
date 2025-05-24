package com.codeurjc.backend.model.API;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuinielaAPI {
    
    private String fecha_sorteo;
    private String game_id;
    private String combinacion;     //"1 - X - 1 - 1 - X - 1 - 1 - X - X - 1 - 1 - 2 - X - 1 - M2",     2 last numbers are from match 15 team1-team2
    private List<Escrutinio> escrutinio;    
 
    public QuinielaAPI(){
    }


    public String getFecha_sorteo() {
        return fecha_sorteo;
    }
    public void setFecha_sorteo(String fecha_sorteo) {
        this.fecha_sorteo = fecha_sorteo;
    }
    public String getGame_id() {
        return game_id;
    }
    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }
    public String getCombinacion() {
        return combinacion;
    }
    public void setCombinacion(String combinacion) {
        this.combinacion = combinacion;
    }
    public List<Escrutinio> getEscrutinio() {
        return escrutinio;
    }
    public void setEscrutinio(List<Escrutinio> escrutinio) {
        this.escrutinio = escrutinio;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Escrutinio{            

        private Integer categoria;      //1=15(dos ultimas tambien), 2=14, 3=13, 4=12, 5=11, 6=10
        private String premio;

        public Escrutinio(){}

        
        public Integer getCategoria() {
            return categoria;
        }
        public void setCategoria(Integer categoria) {
            this.categoria = categoria;
        }
        public String getPremio() {
            return premio;
        }
        public void setPremio(String premio) {
            this.premio = premio;
        }

    }
}