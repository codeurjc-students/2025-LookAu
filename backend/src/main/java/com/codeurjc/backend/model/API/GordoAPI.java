package com.codeurjc.backend.model.API;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GordoAPI{

    private String fecha_sorteo;
    private String game_id;
    private String combinacion;     //"12 - 19 - 49 - 51 - 54 R(5)",
    private List<Escrutinio> escrutinio;    
 
    public GordoAPI(){
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

        private Integer categoria;      //1=5+1, 2=5+0, 3=4+1, 4=4+0, 5=3+1, 6=3+0, 7=2+1, 8=2+0, 9=R
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