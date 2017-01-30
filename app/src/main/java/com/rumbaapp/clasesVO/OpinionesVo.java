package com.rumbaapp.clasesVO;

/**
 * Created by Andres Rangel on 24/01/2016.
 */
public class OpinionesVo {

    private int cod;
    private String nombreSitio;
    private float calificacion;
    private String titulo;
    private String opinion;
    private String imagenPerfil;
    private String miNombre;
    public OpinionesVo() {
    }

    public String getMiNombre() {
        return miNombre;
    }

    public void setMiNombre(String miNombre) {
        this.miNombre = miNombre;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
