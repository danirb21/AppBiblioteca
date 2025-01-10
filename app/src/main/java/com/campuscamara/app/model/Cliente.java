package com.campuscamara.app.model;

import java.util.HashMap;
import java.util.Map;

public class Cliente {
    private String id;
    private String nombre;
    private String apellidos;
    private String dni;
    private long telefono;

    public Cliente(){

    }
    public Cliente(String n, String apellidos, String dni, long telefono){
        this.nombre=n;
        this.apellidos=apellidos;
        this.dni=dni;
        this.telefono=telefono;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Map<String,Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("nombre", getNombre());
        result.put("apellidos", getApellidos());
        result.put("dni",getDni());
        result.put("telefono",getTelefono());
        return result;
    }
}
