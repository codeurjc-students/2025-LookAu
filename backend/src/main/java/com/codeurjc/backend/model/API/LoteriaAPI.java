package com.codeurjc.backend.model.API;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoteriaAPI{

    private String fecha_sorteo;
    private String game_id;
    private Premio primerPremio;    //"71634"
    private Premio segundoPremio;    
    private List<Premio> tercerosPremios;    
    private List<Premio> cuartosPremios;
    private List<Premio> quintosPremios;
    private List<Premio> extraccionesDeCuatroCifras;   //"9551"
    private List<Premio> extraccionesDeTresCifras;   //"006"
    private List<Premio> extraccionesDeDosCifras;   //"03" 
    private List<Premio> reintegros;   //"0"       
 
    public LoteriaAPI(){
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
    public Premio getPrimerPermio() {
        return primerPremio;
    }
    public void setPrimerPermio(Premio primerPermio) {
        this.primerPremio = primerPermio;
    }
    public Premio getSegundoPremio() {
        return segundoPremio;
    }
    public void setSegundoPremio(Premio segundoPremio) {
        this.segundoPremio = segundoPremio;
    }
    public List<Premio> getTercerosPremios() {
        return tercerosPremios;
    }
    public void setTercerosPremios(List<Premio> tercerosPremios) {
        this.tercerosPremios = tercerosPremios;
    }
    public List<Premio> getCuartosPremios() {
        return cuartosPremios;
    }
    public void setCuartosPremios(List<Premio> cuartosPremios) {
        this.cuartosPremios = cuartosPremios;
    }
    public List<Premio> getQuintosPremios() {
        return quintosPremios;
    }
    public void setQuintosPremios(List<Premio> quintosPremios) {
        this.quintosPremios = quintosPremios;
    }
    public List<Premio> getExtraccionesDeCuatroCifras() {
        return extraccionesDeCuatroCifras;
    }
    public void setExtraccionesDeCuatroCifras(List<Premio> extraccionesDeCuatroCifras) {
        this.extraccionesDeCuatroCifras = extraccionesDeCuatroCifras;
    }
    public List<Premio> getExtraccionesDeTresCifras() {
        return extraccionesDeTresCifras;
    }
    public void setExtraccionesDeTresCifras(List<Premio> extraccionesDeTresCifras) {
        this.extraccionesDeTresCifras = extraccionesDeTresCifras;
    }
    public List<Premio> getExtraccionesDeDosCifras() {
        return extraccionesDeDosCifras;
    }
    public void setExtraccionesDeDosCifras(List<Premio> extraccionesDeDosCifras) {
        this.extraccionesDeDosCifras = extraccionesDeDosCifras;
    }
    public List<Premio> getReintegros() {
        return reintegros;
    }
    public void setReintegros(List<Premio> reintegros) {
        this.reintegros = reintegros;
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Premio{

        private Integer decimo;
        private Integer prize;
        
        public Premio(){}

        public Integer getDecimo() {
            return decimo;
        }
        public void setDecimo(Integer decimo) {
            this.decimo = decimo;
        }
        public Integer getPrize() {
            return prize;
        }
        public void setPrize(Integer prize) {
            this.prize = prize;
        }
    }
}