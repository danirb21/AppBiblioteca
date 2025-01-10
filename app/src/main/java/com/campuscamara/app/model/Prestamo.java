package com.campuscamara.app.model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prestamo {
    private String id;
    private GregorianCalendar fechaPrestamo=new GregorianCalendar();
    private GregorianCalendar fechaDevolucion=new GregorianCalendar();
    private String uidUsuario;
    private List<String> idLibros=new ArrayList<>();

    public Prestamo(){

    }
    public Prestamo(String fechaPrestamo, String fechaDev, String uidUsuario,List<String> libros){
        this.setFechaPrestamo(fechaPrestamo);
        this.setFechaDevolucion(fechaDev);
        this.setUidUsuario(uidUsuario);
        this.setLibrosIds(libros);

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo.get(GregorianCalendar.DAY_OF_MONTH)+"/"+fechaPrestamo.get(GregorianCalendar.MONTH)+"/"
                +fechaPrestamo.get(GregorianCalendar.YEAR);
    }

    public void setFechaPrestamo(String fechaAlquiler) {
        String[] fecha=fechaAlquiler.split("/");
        this.fechaPrestamo.set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(fecha[0]));
        this.fechaPrestamo.set(GregorianCalendar.MONTH, Integer.parseInt(fecha[1]));
        this.fechaPrestamo.set(GregorianCalendar.YEAR, Integer.parseInt(fecha[2]));
    }

    public String getFechaDevolucion() {
        return fechaDevolucion.get(GregorianCalendar.DAY_OF_MONTH)+"/"+fechaDevolucion.get(GregorianCalendar.MONTH)+"/"
                +fechaDevolucion.get(GregorianCalendar.YEAR);
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        String[] fecha=fechaDevolucion.split("/");
        this.fechaDevolucion.set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(fecha[0]));
        this.fechaDevolucion.set(GregorianCalendar.MONTH, Integer.parseInt(fecha[1]));
        this.fechaDevolucion.set(GregorianCalendar.YEAR, Integer.parseInt(fecha[2]));
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }


    public Map<String, Object>toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("fechaPrestamo", getFechaPrestamo());
        result.put("fechaDevolucion", getFechaDevolucion());
        result.put("uidUsuario",getUidUsuario());
        result.put("idLibros",new ArrayList<String>(getLibrosIds()));
        return result;
    }

    public List<String> getLibrosIds() {
        return idLibros;
    }

    public void setLibrosIds(List<String> librosIds) {
        this.idLibros = librosIds;
    }
}
