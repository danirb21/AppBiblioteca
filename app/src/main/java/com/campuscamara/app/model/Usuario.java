package com.campuscamara.app.model;

public class Usuario {
    private String username;
    private String password;
    private boolean esPersonal;

    public Usuario(){

    }
    public Usuario(String username, String password, boolean esPersonal){
        this.username=username;
        this.password=password;
        this.esPersonal=esPersonal;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEsPersonal() {
        return esPersonal;
    }

    public void setEsPersonal(boolean esPersonal) {
        this.esPersonal = esPersonal;
    }

}
