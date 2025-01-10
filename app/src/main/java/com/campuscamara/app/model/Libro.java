package com.campuscamara.app.model;

import androidx.annotation.Nullable;

import com.campuscamara.app.frontend.RecyclerAdapter;

import java.util.HashMap;
import java.util.Map;

public class Libro {

    private String id;
    private String titulo;
    private String autor;
    private String genero;
    private String tipo;

    private String coverurl;
    private int totalValor;

    public Libro(){

    }
    public Libro(String titulo, String autor, String genero, String tipo, int totalValor){
        this.setTitulo(titulo);
        this.setAutor(autor);
        this.setGenero(genero);
        this.setTipo(tipo);
        this.setTotalValor(totalValor);
    }
    public Libro(String titulo, String autor, String genero, String tipo){
        this.setTitulo(titulo);
        this.setAutor(autor);
        this.setGenero(genero);
        this.setTipo(tipo);
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(int totalValor) {
        this.totalValor = totalValor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("titulo", titulo);
        result.put("autor", autor);
        result.put("genero",genero);
        result.put("tipo",tipo);
        result.put("coverurl",coverurl);
        result.put("totalValor",totalValor);
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean bol=false;
        if(obj instanceof Libro || obj instanceof RecyclerAdapter.LibrosIsbn){
            if(obj instanceof Libro){
                if(((Libro) obj).id.equals(this.id)){
                    bol=true;
                }
            }else{
                if(((RecyclerAdapter.LibrosIsbn) obj).getId().equals(this.id)){
                    bol=true;
                }
            }
        }
        return bol;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }
}
