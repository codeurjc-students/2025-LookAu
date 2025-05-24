package com.codeurjc.backend.model.API;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimitivaAPI{

    private String fecha_sorteo;
    private String game_id;
    private String combinacion;     //"1 - 4 - 13 - 18 - 24 - 28 C(8) R(2)",
    private List<Escrutinio> escrutinio; 
    private Joker joker;   


    public PrimitivaAPI(){
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
    public Joker getJoker() {
        return joker;
    }
    public void setJoker(Joker joker) {
        this.joker = joker;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Joker{
        
        private String combinacion;

        public Joker(){}

        public String getCombinacion() {
            return combinacion;
        }
        public void setCombinacion(String combinacion) {
            this.combinacion = combinacion;
        }
        
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Escrutinio{            

        private Integer categoria;      //1=6+1, 2=6+0, 3=5+C, 4=5+0, 5=4+0, 6=3+0, 8=R
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