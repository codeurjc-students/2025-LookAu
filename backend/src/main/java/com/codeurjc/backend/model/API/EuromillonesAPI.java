package com.codeurjc.backend.model.API;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EuromillonesAPI{

    private String fecha_sorteo;
    private String game_id;
    private String combinacion;     //" 01 - 08 - 13 - 29 - 47 - 05 - 06" -  2 last numbers are stars
    private List<Escrutinio> escrutinio;    
 
    public EuromillonesAPI(){
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

        private Integer categoria;      //1=5+2, 2=5+1, 3=5+0, 4=4+2, 5=4+1, 6=3+2, 7=4+0, 8=2+2, 9=3+1, 10=3+0, 11=1+2, 12=2+1, 13=2+0
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