package com.example.trab1;

public class Celular {
    private  String marca_;
    private  String modelo_;
    private  String marcaId_;

    public Celular(String marca_, String modelo_, String marcaId_) {
        this.marca_ = marca_;
        this.modelo_ = modelo_;
        this.marcaId_ = marcaId_;
    }

    public String getMarca_() {
        return marca_;
    }

    public String getModelo_() {
        return modelo_;
    }

    public String getMarcaId_() {
        return marcaId_;
    }
}