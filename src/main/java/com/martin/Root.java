package com.martin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Root implements Serializable {
    @JsonProperty("clases")
    List<Clases> clases;
    @JsonProperty("sub-clases")
    List<SubClases> subClases;

    public Root() {
    }

    public Root(List<Clases> clases, List<SubClases> subClases) {
        this.clases = clases;
        this.subClases = subClases;
    }

    public List<Clases> getClases() {
        return clases;
    }

    public void setClases(List<Clases> clases) {
        this.clases = clases;
    }

    public List<SubClases> getSubClases() {
        return subClases;
    }

    public void setSubClases(List<SubClases> subClases) {
        this.subClases = subClases;
    }
}
