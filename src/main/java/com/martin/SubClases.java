package com.martin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SubClases implements Serializable {
    @JsonProperty("name-clase")
    String id;
    @JsonProperty("name-subclase")
    String name;
    @JsonProperty("mini-description")
    String descripcion;
    @JsonProperty("imagen")
    String imagen;

    public SubClases() {
    }

    public SubClases(String id, String name, String descripcion, String imagen) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "SubClases{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
