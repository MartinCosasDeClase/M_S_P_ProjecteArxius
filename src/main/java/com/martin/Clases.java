package com.martin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Clases {
    @JsonProperty("name")
    String name;
    @JsonProperty("mini-description")
    String descripcion;
    @JsonProperty("imagen")
    String imagen;

    public Clases() {
    }

    public Clases(String name, String descripcion, String imagen) {
        this.name = name;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Clases{" +
                "name='" + name + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
