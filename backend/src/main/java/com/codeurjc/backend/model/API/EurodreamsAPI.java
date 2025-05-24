package com.codeurjc.backend.model.API;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EurodreamsAPI{

    private String fecha_sorteo;
    private String game_id;
    private String combinacion;     //"08 - 15 - 26 - 32 - 35 - 38 C(2)",
    private List<Escrutinio> escrutinio; 
 
    public EurodreamsAPI(){
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

    //category 1: 6+1 20.000€ mensuales durante 30 años
    //category 2: 2.000 € mensuales durante 5 años


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Escrutinio{            

        private Integer categoria;      //1=6+1, 2=6, 3=5, 4=4, 5=3, 6=2
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



