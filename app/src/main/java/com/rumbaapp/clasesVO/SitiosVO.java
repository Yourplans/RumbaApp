package com.rumbaapp.clasesVO;

/**
 * Created by Rangel on 13/05/2016.
 */
public class SitiosVO {


    private int codSitio;
    private String imagenSitio;
    private String nombreSitio;
    private String direccionSitio;
    private float calificacionSitio;
    private String latitud;
    private String longitud;


    public void setLatitud(String latitud){

        this.latitud=latitud;
    }
    public String getLatitud(){
        return latitud;
    }

    public void setLongitud(String longitud){
        this.longitud=longitud;
    }
    public String getLongitud(){
        return longitud;
    }


    public SitiosVO() {
    }

    public int getCodSitio() {
        return codSitio;
    }

    public void setCodSitio(int codSitio) {
        this.codSitio = codSitio;
    }

    public String getImagenSitio() {
        return imagenSitio;
    }

    public void setImagenSitio(String imagenSitio) {
        this.imagenSitio = imagenSitio;
    }

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public String getDireccionSitio() {
        return direccionSitio;
    }

    public void setDireccionSitio(String direccionSitio) {
        this.direccionSitio = direccionSitio;
    }

    public float getCalificacionSitio() {
        return calificacionSitio;
    }

    public void setCalificacionSitio(float calificacionSitio) {
        this.calificacionSitio = calificacionSitio;
    }
}
